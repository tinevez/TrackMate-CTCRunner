/*-
 * #%L
 * Fiji distribution of ImageJ for the life sciences.
 * %%
 * Copyright (C) 2021 - 2022 The Institut Pasteur.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package fiji.plugin.trackmate.ctc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.scijava.listeners.Listeners;

import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.ctc.model.AbstractSweepModel.ModelListener;
import fiji.plugin.trackmate.ctc.model.detector.CellposeDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.DetectorSweepModel;
import fiji.plugin.trackmate.ctc.model.detector.DogDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.IlastikDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.LabelImgDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.LogDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.MaskDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.MorphoLibJDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.StarDistCustomDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.StarDistDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.ThresholdDetectorModel;
import fiji.plugin.trackmate.ctc.model.detector.WekaDetectorModel;
import fiji.plugin.trackmate.ctc.model.filter.FilterConfigModel;
import fiji.plugin.trackmate.ctc.model.tracker.KalmanTrackerModel;
import fiji.plugin.trackmate.ctc.model.tracker.LAPTrackerModel;
import fiji.plugin.trackmate.ctc.model.tracker.NearestNeighborTrackerModel;
import fiji.plugin.trackmate.ctc.model.tracker.OverlapTrackerModel;
import fiji.plugin.trackmate.ctc.model.tracker.SimpleLAPTrackerModel;
import fiji.plugin.trackmate.ctc.model.tracker.TrackerSweepModel;

public class ParameterSweepModel
{

	private final transient Listeners.List< ModelListener > modelListeners;

	private final Map< String, DetectorSweepModel > detectorModels = new LinkedHashMap<>();

	private final Map< String, TrackerSweepModel > trackerModels = new LinkedHashMap<>();

	private final Map< String, Boolean > active = new HashMap<>();

	private final FilterConfigModel spotFilters = new FilterConfigModel();

	private final FilterConfigModel trackFilters = new FilterConfigModel();

	public ParameterSweepModel()
	{
		modelListeners = new Listeners.SynchronizedList<>();

		// Detectors.
		add( new LogDetectorModel() );
		add( new DogDetectorModel() );
		add( new MaskDetectorModel() );
		add( new ThresholdDetectorModel() );
		add( new LabelImgDetectorModel() );

		// Optional detector modules. Stuff that might not be installed.
		add( new MorphoLibJDetectorModel() );
		add( new IlastikDetectorModel() );
		add( new CellposeDetectorModel() );
		add( new WekaDetectorModel() );
		add( new StarDistDetectorModel() );
		add( new StarDistCustomDetectorModel() );

		// Trackers.
		add( new SimpleLAPTrackerModel() );
		add( new LAPTrackerModel() );
		add( new KalmanTrackerModel() );
		add( new OverlapTrackerModel() );
		add( new NearestNeighborTrackerModel() );

		// Default: everything is inactive.
		for ( final TrackerSweepModel m : trackerModels.values() )
			active.put( m.getName(), Boolean.FALSE );
		for ( final DetectorSweepModel m : detectorModels.values() )
			active.put( m.getName(), Boolean.FALSE );

		registerListeners();
	}

	public void registerListeners()
	{
		// Forward component changes to listeners.
		detectorModels().forEach( model -> model.listeners().add( () -> notifyListeners() ) );
		trackerModels().forEach( model -> model.listeners().add( () -> notifyListeners() ) );
	}

	private void add( final DetectorSweepModel model )
	{
		this.detectorModels.put( model.getName(), model );
	}

	private void add( final TrackerSweepModel model )
	{
		this.trackerModels.put( model.getName(), model );
	}

	public Collection< DetectorSweepModel > detectorModels()
	{
		return detectorModels.values();
	}

	public Collection< TrackerSweepModel > trackerModels()
	{
		return trackerModels.values();
	}

	public boolean isActive( final String name )
	{
		final Boolean val = active.get( name );
		if ( val == null )
			throw new IllegalArgumentException( "Unregistered model with name: " + name );

		return val.booleanValue();
	}

	public void setActive( final String name, final boolean active )
	{
		final Boolean previous = this.active.put( name, Boolean.valueOf( active ) );
		if ( active != previous.booleanValue() )
			notifyListeners();
	}

	public FilterConfigModel getSpotFilters()
	{
		return spotFilters;
	}

	public FilterConfigModel getTrackFilters()
	{
		return trackFilters;
	}

	public List< DetectorSweepModel > getActiveDetectors()
	{
		final List< DetectorSweepModel > activeDetectors = new ArrayList<>();
		for ( final String name : detectorModels.keySet() )
			if ( isActive( name ) )
				activeDetectors.add( detectorModels.get( name ) );

		return activeDetectors;
	}

	public List< TrackerSweepModel > getActiveTracker()
	{
		final List< TrackerSweepModel > activeTrackers = new ArrayList<>();
		for ( final String name : trackerModels.keySet() )
			if ( isActive( name ) )
				activeTrackers.add( trackerModels.get( name ) );

		return activeTrackers;
	}

	/**
	 * Returns the count of the different settings that will be generated from
	 * this model.
	 * 
	 * @return the count of settings.
	 */
	public int count()
	{
		return countDetectorSettings() * countTrackerSettings();
	}

	/**
	 * Returns the count of the different tracker settings that will be
	 * generated from this model.
	 * 
	 * @return the count of settings.
	 */
	public int countTrackerSettings()
	{
		final int targetChannel = 1;
		final Settings base = new Settings( null );
		int count = 0;
		for ( final TrackerSweepModel trackerModel : getActiveTracker() )
		{
			final Iterator< Settings > tit = trackerModel.iterator( base, targetChannel );
			while ( tit.hasNext() )
			{
				tit.next();
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns the count of the different detector settings that will be
	 * generated from this model.
	 * 
	 * @return the count of settings.
	 */
	public int countDetectorSettings()
	{
		final int targetChannel = 1;
		final Settings base = new Settings( null );
		int count = 0;
		for ( final DetectorSweepModel detectorModel : getActiveDetectors() )
		{
			final Iterator< Settings > dit = detectorModel.iterator( base, targetChannel );
			while ( dit.hasNext() )
			{
				dit.next();
				count++;
			}
		}
		return count;
	}

	public Listeners.List< ModelListener > listeners()
	{
		return modelListeners;
	}

	protected void notifyListeners()
	{
		for ( final ModelListener l : modelListeners.list )
			l.modelChanged();
	}
}
