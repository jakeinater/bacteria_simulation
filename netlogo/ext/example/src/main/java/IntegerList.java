import org.nlogo.api.*;
import org.nlogo.core.Syntax;
import org.nlogo.core.SyntaxJ;

public class IntegerList implements Reporter {
	
	public Syntax getSyntax() {
		return SyntaxJ.reporterSyntax(
				new int[] { Syntax.NumberType() }, Syntax.ListType() );
	}

	public Object report(Argument args[], Context context)
		throws ExtensionException
	{
		LogoListBuilder list = new LogoListBuilder();

		int n;

		try {
			n = args[0].getIntValue();
		}
		catch(LogoException e) {
			throw new ExtensionException( e.getMessage() );
		}

		if ( n < 0 ){
			throw new ExtensionException
				("input must be positive");
		}

		for (int i = 0; i < n; i++){
			list.add(Double.valueOf(i));
		}
		return list.toLogoList();
	}
}

		

