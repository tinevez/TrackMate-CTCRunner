package fiji.plugin.trackmate.ctc.ui;

import javax.swing.JFrame;

import org.scijava.Context;

import fiji.plugin.trackmate.gui.Icons;

public class ParameterSweepController
{

	private final ParameterSweepPanel gui;

	private final JFrame frame;

	public ParameterSweepController( final Context context )
	{
		gui = new ParameterSweepPanel( context );
		frame = new JFrame( "TrackMate parameter sweep" );
		frame.setIconImage( Icons.TRACKMATE_ICON.getImage() );
		frame.getContentPane().add( gui );
		frame.pack();
		frame.setLocationRelativeTo( null );
	}

	public void show()
	{
		frame.setVisible( true );
	}
}
