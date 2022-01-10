package fiji.plugin.trackmate.ctc.ui;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fiji.plugin.trackmate.ctc.TrackMateParameterSweepPlugin;
import net.imagej.ImageJ;

public class GuiTestDrive
{

	public static void main( final String[] args ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		final ImageJ ij = new ImageJ();
		ij.launch( args );

//		final String rootFolder = "D:\\Projects\\JYTinevez\\TrackMate-StarDist\\CTCMetrics\\TCells\\JY";
//		final String sourceImagePath = new File( rootFolder, "TCellsMigration.tif" ).getAbsolutePath();
//		final File groundTruthPath = new File( rootFolder, "01_GT" );

		// final String rootFolder =
		// "D:\\Projects\\JYTinevez\\TrackMate-StarDist\\CTCMetrics\\CellMigration";
//		final String sourceImagePath = new File( rootFolder, "CellMigration.tif" ).getAbsolutePath();
//		final File groundTruthPath = new File( rootFolder, "02_GT" );

		final String rootFolder = "D:\\Projects\\JYTinevez\\TrackMate-StarDist\\CTCMetrics\\NMeningitidis";
		final String sourceImagePath = new File( rootFolder, "NeisseriaMeningitidisGrowth.tif" ).getAbsolutePath();
		final File groundTruthPath = new File( rootFolder, "01_GT" );

//		final String rootFolder = "D:\\Projects\\JYTinevez\\TrackMate-StarDist\\CTCMetrics\\";
//		final String sourceImagePath = new File( rootFolder, "NMeningitidis/NeisseriaMeningitidisGrowth.tif" ).getAbsolutePath();
//		final File groundTruthPath = new File( rootFolder, "01_GT" );

		final TrackMateParameterSweepPlugin plugin = new TrackMateParameterSweepPlugin();
		plugin.run( sourceImagePath + ", " + groundTruthPath );

//		IJ.openImage( sourceImagePath ).show();
//		plugin.run( "" );
	}
}
