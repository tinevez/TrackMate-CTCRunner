package fiji.plugin.trackmate.ctc.model.filter;

import org.scijava.listeners.Listeners;

import fiji.plugin.trackmate.ctc.model.AbstractSweepModel.ModelListener;
import fiji.plugin.trackmate.features.FeatureFilter;

public class FeatureFilterModel
{

	private final transient Listeners.List< ModelListener > modelListeners;

	private String feature;

	private double threshold;

	private boolean isAbove;

	public FeatureFilterModel()
	{
		this.modelListeners = new Listeners.SynchronizedList<>();
	}

	public String getFeature()
	{
		return feature;
	}

	public void feature( final String feature )
	{
		if ( this.feature != feature )
		{
			this.feature = feature;
			notifyListeners();
		}
	}

	public boolean isAbove()
	{
		return isAbove;
	}

	public void setAbove( final boolean isAbove )
	{
		if ( this.isAbove != isAbove )
		{
			this.isAbove = isAbove;
			notifyListeners();
		}
	}

	public double getThreshold()
	{
		return threshold;
	}

	public void threshold( final double threshold )
	{
		if ( this.threshold != threshold )
		{
			this.threshold = threshold;
			notifyListeners();
		}
	}

	public FeatureFilter toFeatureFilter()
	{
		return new FeatureFilter( feature, threshold, isAbove );
	}

	private void notifyListeners()
	{
		for ( final ModelListener l : modelListeners.list )
			l.modelChanged();
	}

	public Listeners< ModelListener > listeners()
	{
		return modelListeners;
	}
}
