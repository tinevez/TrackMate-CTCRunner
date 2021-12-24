package fiji.plugin.trackmate.ctc.ui.components;

import java.util.ArrayList;
import java.util.List;

public class StringRangeParamSweepModel extends AbstractParamSweepModel< String >
{

	protected final List< String > stringList = new ArrayList<>();

	private boolean isFile;

	@Override
	public StringRangeParamSweepModel paramName( final String paramName )
	{
		return ( StringRangeParamSweepModel ) super.paramName( paramName );
	}

	public StringRangeParamSweepModel add( final String string )
	{
		stringList.add( string );
		notifyListeners();
		return this;
	}

	public StringRangeParamSweepModel remove( final int index )
	{
		if ( stringList.size() < 2 || index < 0 || index >= stringList.size() )
			return this;

		stringList.remove( index );
		notifyListeners();
		return this;
	}

	public StringRangeParamSweepModel set( final int id, final String text )
	{
		if ( !stringList.get( id ).equals( text ) )
		{
			stringList.set( id, text );
			notifyListeners();
		}
		return this;
	}

	public StringRangeParamSweepModel isFile( final boolean isFile )
	{
		if ( this.isFile != isFile )
		{
			this.isFile = isFile;
			notifyListeners();
		}
		return this;
	}

	@Override
	public List< String > getRange()
	{
		return new ArrayList<>( stringList );
	}

	@Override
	public String toString()
	{
		final StringBuilder str = new StringBuilder();
		str.append( String.format( "%s:\n", paramName ) );
		int index = 0;
		for ( final String string : stringList )
			str.append( String.format( " - %2d: %s\n", index++, string ) );
		return str.toString();
	}

	public boolean isFile()
	{
		return isFile;
	}

}