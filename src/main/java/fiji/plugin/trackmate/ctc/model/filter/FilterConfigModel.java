package fiji.plugin.trackmate.ctc.model.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.scijava.listeners.Listeners;

import fiji.plugin.trackmate.ctc.model.AbstractSweepModel.ModelListener;
import fiji.plugin.trackmate.features.FeatureFilter;

public class FilterConfigModel implements Iterable< FeatureFilterModel >
{

	private transient Listeners.List< ModelListener > modelListeners;

	private final List< FeatureFilterModel > models;

	public FilterConfigModel()
	{
		this( new ArrayList<>() );
	}

	public FilterConfigModel( final List< FeatureFilterModel > models )
	{
		this.models = models;
		// Register models.
		models.forEach( m -> m.listeners().add( () -> notifyListeners() ) );
	}

	public Listeners.List< ModelListener > listeners()
	{
		if ( modelListeners == null )
		{
			/*
			 * Work around the listeners field being null after deserialization.
			 * We also need to register again the sub-models.
			 */
			this.modelListeners = new Listeners.SynchronizedList<>();
			for ( final FeatureFilterModel model : models )
				model.listeners().add( () -> notifyListeners() );
		}
		return modelListeners;
	}

	protected void notifyListeners()
	{
		for ( final ModelListener l : modelListeners.list )
			l.modelChanged();
	}

	@Override
	public Iterator< FeatureFilterModel > iterator()
	{
		return models.iterator();
	}

	public void clear()
	{
		models.clear();
		notifyListeners();
	}

	public List< FeatureFilterModel > getModels()
	{
		return models;
	}

	public void add( final FeatureFilterModel filter )
	{
		models.add( filter );
		notifyListeners();
	}

	public void remove( final FeatureFilterModel featureFilterModel )
	{
		final int id = models.lastIndexOf( featureFilterModel );
		if ( id < 0 )
			return;
		models.remove( id );
		notifyListeners();
	}

	public List< FeatureFilter > toFeatureFilters()
	{
		return models.stream().map( FeatureFilterModel::toFeatureFilter ).collect( Collectors.toList() );
	}
}
