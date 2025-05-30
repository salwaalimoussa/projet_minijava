// Generated from MiniCLexer.g4 by ANTLR 4.7.2

package fr.n7.stl.minic.parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MiniCLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Egal=1, AccoladeOuvrante=2, AccoladeFermante=3, ParentheseOuvrante=4, 
		ParentheseFermante=5, CrochetOuvrant=6, CrochetFermant=7, Point=8, PointInterrogation=9, 
		DeuxPoint=10, Virgule=11, PointVirgule=12, Afficher=13, Si=14, Sinon=15, 
		Retour=16, DefinitionConstante=17, DefinitionType=18, Enregistrement=19, 
		Enumeration=20, Nouveau=21, TantQue=22, Asterisque=23, Oblique=24, PourCent=25, 
		Plus=26, Moins=27, DoubleBarre=28, DoubleEsperluette=29, PointExclamation=30, 
		Inferieur=31, Superieur=32, InferieurEgal=33, SuperieurEgal=34, DoubleEgal=35, 
		ExclamationEgal=36, Esperluette=37, TypeEntier=38, TypeFlottant=39, TypeBooleen=40, 
		TypeCaractere=41, TypeChaine=42, TypeVide=43, Vrai=44, Faux=45, Nul=46, 
		Premier=47, Second=48, Caractere=49, Chaine=50, Identificateur=51, Entier=52, 
		Flottant=53, CommentaireLigne=54, CommentaireBloc=55, WS=56;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"Egal", "AccoladeOuvrante", "AccoladeFermante", "ParentheseOuvrante", 
			"ParentheseFermante", "CrochetOuvrant", "CrochetFermant", "Point", "PointInterrogation", 
			"DeuxPoint", "Virgule", "PointVirgule", "Afficher", "Si", "Sinon", "Retour", 
			"DefinitionConstante", "DefinitionType", "Enregistrement", "Enumeration", 
			"Nouveau", "TantQue", "Asterisque", "Oblique", "PourCent", "Plus", "Moins", 
			"DoubleBarre", "DoubleEsperluette", "PointExclamation", "Inferieur", 
			"Superieur", "InferieurEgal", "SuperieurEgal", "DoubleEgal", "ExclamationEgal", 
			"Esperluette", "TypeEntier", "TypeFlottant", "TypeBooleen", "TypeCaractere", 
			"TypeChaine", "TypeVide", "Vrai", "Faux", "Nul", "Premier", "Second", 
			"Caractere", "Chaine", "Identificateur", "Entier", "Flottant", "CommentaireLigne", 
			"CommentaireBloc", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", "'{'", "'}'", "'('", "')'", "'['", "']'", "'.'", "'?'", 
			"':'", "','", "';'", "'print'", "'if'", "'else'", "'return'", "'const'", 
			"'typedef'", "'struct'", "'enum'", "'new'", "'while'", "'*'", "'/'", 
			"'%'", "'+'", "'-'", "'||'", "'&&'", "'!'", "'<'", "'>'", "'<='", "'>='", 
			"'=='", "'!='", "'&'", "'int'", "'float'", "'boolean'", "'char'", "'String'", 
			"'void'", "'true'", "'false'", "'null'", "'fst'", "'snd'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Egal", "AccoladeOuvrante", "AccoladeFermante", "ParentheseOuvrante", 
			"ParentheseFermante", "CrochetOuvrant", "CrochetFermant", "Point", "PointInterrogation", 
			"DeuxPoint", "Virgule", "PointVirgule", "Afficher", "Si", "Sinon", "Retour", 
			"DefinitionConstante", "DefinitionType", "Enregistrement", "Enumeration", 
			"Nouveau", "TantQue", "Asterisque", "Oblique", "PourCent", "Plus", "Moins", 
			"DoubleBarre", "DoubleEsperluette", "PointExclamation", "Inferieur", 
			"Superieur", "InferieurEgal", "SuperieurEgal", "DoubleEgal", "ExclamationEgal", 
			"Esperluette", "TypeEntier", "TypeFlottant", "TypeBooleen", "TypeCaractere", 
			"TypeChaine", "TypeVide", "Vrai", "Faux", "Nul", "Premier", "Second", 
			"Caractere", "Chaine", "Identificateur", "Entier", "Flottant", "CommentaireLigne", 
			"CommentaireBloc", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public MiniCLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MiniCLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2:\u0165\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\3\2\3\2\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35"+
		"\3\35\3\36\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3\"\3#\3#\3#\3$\3$"+
		"\3$\3%\3%\3%\3&\3&\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3"+
		")\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3-\3-\3-\3"+
		"-\3-\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\60\3\61\3\61\3"+
		"\61\3\61\3\62\3\62\3\62\3\62\3\63\3\63\7\63\u012a\n\63\f\63\16\63\u012d"+
		"\13\63\3\63\3\63\3\64\3\64\7\64\u0133\n\64\f\64\16\64\u0136\13\64\3\65"+
		"\3\65\3\65\7\65\u013b\n\65\f\65\16\65\u013e\13\65\5\65\u0140\n\65\3\66"+
		"\3\66\3\66\3\66\3\67\3\67\3\67\3\67\7\67\u014a\n\67\f\67\16\67\u014d\13"+
		"\67\3\67\3\67\38\38\38\38\78\u0155\n8\f8\168\u0158\138\38\38\38\38\38"+
		"\39\69\u0160\n9\r9\169\u0161\39\39\4\u012b\u0156\2:\3\3\5\4\7\5\t\6\13"+
		"\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'"+
		"\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'"+
		"M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:\3\2\b\4\2C\\c|"+
		"\6\2\62;C\\aac|\3\2\63;\3\2\62;\4\2\f\f\17\17\5\2\13\f\17\17\"\"\2\u016b"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2"+
		"\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3"+
		"\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2"+
		"\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2"+
		"U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3"+
		"\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2"+
		"\2\2o\3\2\2\2\2q\3\2\2\2\3s\3\2\2\2\5u\3\2\2\2\7w\3\2\2\2\ty\3\2\2\2\13"+
		"{\3\2\2\2\r}\3\2\2\2\17\177\3\2\2\2\21\u0081\3\2\2\2\23\u0083\3\2\2\2"+
		"\25\u0085\3\2\2\2\27\u0087\3\2\2\2\31\u0089\3\2\2\2\33\u008b\3\2\2\2\35"+
		"\u0091\3\2\2\2\37\u0094\3\2\2\2!\u0099\3\2\2\2#\u00a0\3\2\2\2%\u00a6\3"+
		"\2\2\2\'\u00ae\3\2\2\2)\u00b5\3\2\2\2+\u00ba\3\2\2\2-\u00be\3\2\2\2/\u00c4"+
		"\3\2\2\2\61\u00c6\3\2\2\2\63\u00c8\3\2\2\2\65\u00ca\3\2\2\2\67\u00cc\3"+
		"\2\2\29\u00ce\3\2\2\2;\u00d1\3\2\2\2=\u00d4\3\2\2\2?\u00d6\3\2\2\2A\u00d8"+
		"\3\2\2\2C\u00da\3\2\2\2E\u00dd\3\2\2\2G\u00e0\3\2\2\2I\u00e3\3\2\2\2K"+
		"\u00e6\3\2\2\2M\u00e8\3\2\2\2O\u00ec\3\2\2\2Q\u00f2\3\2\2\2S\u00fa\3\2"+
		"\2\2U\u00ff\3\2\2\2W\u0106\3\2\2\2Y\u010b\3\2\2\2[\u0110\3\2\2\2]\u0116"+
		"\3\2\2\2_\u011b\3\2\2\2a\u011f\3\2\2\2c\u0123\3\2\2\2e\u0127\3\2\2\2g"+
		"\u0130\3\2\2\2i\u013f\3\2\2\2k\u0141\3\2\2\2m\u0145\3\2\2\2o\u0150\3\2"+
		"\2\2q\u015f\3\2\2\2st\7?\2\2t\4\3\2\2\2uv\7}\2\2v\6\3\2\2\2wx\7\177\2"+
		"\2x\b\3\2\2\2yz\7*\2\2z\n\3\2\2\2{|\7+\2\2|\f\3\2\2\2}~\7]\2\2~\16\3\2"+
		"\2\2\177\u0080\7_\2\2\u0080\20\3\2\2\2\u0081\u0082\7\60\2\2\u0082\22\3"+
		"\2\2\2\u0083\u0084\7A\2\2\u0084\24\3\2\2\2\u0085\u0086\7<\2\2\u0086\26"+
		"\3\2\2\2\u0087\u0088\7.\2\2\u0088\30\3\2\2\2\u0089\u008a\7=\2\2\u008a"+
		"\32\3\2\2\2\u008b\u008c\7r\2\2\u008c\u008d\7t\2\2\u008d\u008e\7k\2\2\u008e"+
		"\u008f\7p\2\2\u008f\u0090\7v\2\2\u0090\34\3\2\2\2\u0091\u0092\7k\2\2\u0092"+
		"\u0093\7h\2\2\u0093\36\3\2\2\2\u0094\u0095\7g\2\2\u0095\u0096\7n\2\2\u0096"+
		"\u0097\7u\2\2\u0097\u0098\7g\2\2\u0098 \3\2\2\2\u0099\u009a\7t\2\2\u009a"+
		"\u009b\7g\2\2\u009b\u009c\7v\2\2\u009c\u009d\7w\2\2\u009d\u009e\7t\2\2"+
		"\u009e\u009f\7p\2\2\u009f\"\3\2\2\2\u00a0\u00a1\7e\2\2\u00a1\u00a2\7q"+
		"\2\2\u00a2\u00a3\7p\2\2\u00a3\u00a4\7u\2\2\u00a4\u00a5\7v\2\2\u00a5$\3"+
		"\2\2\2\u00a6\u00a7\7v\2\2\u00a7\u00a8\7{\2\2\u00a8\u00a9\7r\2\2\u00a9"+
		"\u00aa\7g\2\2\u00aa\u00ab\7f\2\2\u00ab\u00ac\7g\2\2\u00ac\u00ad\7h\2\2"+
		"\u00ad&\3\2\2\2\u00ae\u00af\7u\2\2\u00af\u00b0\7v\2\2\u00b0\u00b1\7t\2"+
		"\2\u00b1\u00b2\7w\2\2\u00b2\u00b3\7e\2\2\u00b3\u00b4\7v\2\2\u00b4(\3\2"+
		"\2\2\u00b5\u00b6\7g\2\2\u00b6\u00b7\7p\2\2\u00b7\u00b8\7w\2\2\u00b8\u00b9"+
		"\7o\2\2\u00b9*\3\2\2\2\u00ba\u00bb\7p\2\2\u00bb\u00bc\7g\2\2\u00bc\u00bd"+
		"\7y\2\2\u00bd,\3\2\2\2\u00be\u00bf\7y\2\2\u00bf\u00c0\7j\2\2\u00c0\u00c1"+
		"\7k\2\2\u00c1\u00c2\7n\2\2\u00c2\u00c3\7g\2\2\u00c3.\3\2\2\2\u00c4\u00c5"+
		"\7,\2\2\u00c5\60\3\2\2\2\u00c6\u00c7\7\61\2\2\u00c7\62\3\2\2\2\u00c8\u00c9"+
		"\7\'\2\2\u00c9\64\3\2\2\2\u00ca\u00cb\7-\2\2\u00cb\66\3\2\2\2\u00cc\u00cd"+
		"\7/\2\2\u00cd8\3\2\2\2\u00ce\u00cf\7~\2\2\u00cf\u00d0\7~\2\2\u00d0:\3"+
		"\2\2\2\u00d1\u00d2\7(\2\2\u00d2\u00d3\7(\2\2\u00d3<\3\2\2\2\u00d4\u00d5"+
		"\7#\2\2\u00d5>\3\2\2\2\u00d6\u00d7\7>\2\2\u00d7@\3\2\2\2\u00d8\u00d9\7"+
		"@\2\2\u00d9B\3\2\2\2\u00da\u00db\7>\2\2\u00db\u00dc\7?\2\2\u00dcD\3\2"+
		"\2\2\u00dd\u00de\7@\2\2\u00de\u00df\7?\2\2\u00dfF\3\2\2\2\u00e0\u00e1"+
		"\7?\2\2\u00e1\u00e2\7?\2\2\u00e2H\3\2\2\2\u00e3\u00e4\7#\2\2\u00e4\u00e5"+
		"\7?\2\2\u00e5J\3\2\2\2\u00e6\u00e7\7(\2\2\u00e7L\3\2\2\2\u00e8\u00e9\7"+
		"k\2\2\u00e9\u00ea\7p\2\2\u00ea\u00eb\7v\2\2\u00ebN\3\2\2\2\u00ec\u00ed"+
		"\7h\2\2\u00ed\u00ee\7n\2\2\u00ee\u00ef\7q\2\2\u00ef\u00f0\7c\2\2\u00f0"+
		"\u00f1\7v\2\2\u00f1P\3\2\2\2\u00f2\u00f3\7d\2\2\u00f3\u00f4\7q\2\2\u00f4"+
		"\u00f5\7q\2\2\u00f5\u00f6\7n\2\2\u00f6\u00f7\7g\2\2\u00f7\u00f8\7c\2\2"+
		"\u00f8\u00f9\7p\2\2\u00f9R\3\2\2\2\u00fa\u00fb\7e\2\2\u00fb\u00fc\7j\2"+
		"\2\u00fc\u00fd\7c\2\2\u00fd\u00fe\7t\2\2\u00feT\3\2\2\2\u00ff\u0100\7"+
		"U\2\2\u0100\u0101\7v\2\2\u0101\u0102\7t\2\2\u0102\u0103\7k\2\2\u0103\u0104"+
		"\7p\2\2\u0104\u0105\7i\2\2\u0105V\3\2\2\2\u0106\u0107\7x\2\2\u0107\u0108"+
		"\7q\2\2\u0108\u0109\7k\2\2\u0109\u010a\7f\2\2\u010aX\3\2\2\2\u010b\u010c"+
		"\7v\2\2\u010c\u010d\7t\2\2\u010d\u010e\7w\2\2\u010e\u010f\7g\2\2\u010f"+
		"Z\3\2\2\2\u0110\u0111\7h\2\2\u0111\u0112\7c\2\2\u0112\u0113\7n\2\2\u0113"+
		"\u0114\7u\2\2\u0114\u0115\7g\2\2\u0115\\\3\2\2\2\u0116\u0117\7p\2\2\u0117"+
		"\u0118\7w\2\2\u0118\u0119\7n\2\2\u0119\u011a\7n\2\2\u011a^\3\2\2\2\u011b"+
		"\u011c\7h\2\2\u011c\u011d\7u\2\2\u011d\u011e\7v\2\2\u011e`\3\2\2\2\u011f"+
		"\u0120\7u\2\2\u0120\u0121\7p\2\2\u0121\u0122\7f\2\2\u0122b\3\2\2\2\u0123"+
		"\u0124\7)\2\2\u0124\u0125\13\2\2\2\u0125\u0126\7)\2\2\u0126d\3\2\2\2\u0127"+
		"\u012b\7$\2\2\u0128\u012a\13\2\2\2\u0129\u0128\3\2\2\2\u012a\u012d\3\2"+
		"\2\2\u012b\u012c\3\2\2\2\u012b\u0129\3\2\2\2\u012c\u012e\3\2\2\2\u012d"+
		"\u012b\3\2\2\2\u012e\u012f\7$\2\2\u012ff\3\2\2\2\u0130\u0134\t\2\2\2\u0131"+
		"\u0133\t\3\2\2\u0132\u0131\3\2\2\2\u0133\u0136\3\2\2\2\u0134\u0132\3\2"+
		"\2\2\u0134\u0135\3\2\2\2\u0135h\3\2\2\2\u0136\u0134\3\2\2\2\u0137\u0140"+
		"\7\62\2\2\u0138\u013c\t\4\2\2\u0139\u013b\t\5\2\2\u013a\u0139\3\2\2\2"+
		"\u013b\u013e\3\2\2\2\u013c\u013a\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u0140"+
		"\3\2\2\2\u013e\u013c\3\2\2\2\u013f\u0137\3\2\2\2\u013f\u0138\3\2\2\2\u0140"+
		"j\3\2\2\2\u0141\u0142\5i\65\2\u0142\u0143\7\60\2\2\u0143\u0144\5i\65\2"+
		"\u0144l\3\2\2\2\u0145\u0146\7\61\2\2\u0146\u0147\7\61\2\2\u0147\u014b"+
		"\3\2\2\2\u0148\u014a\n\6\2\2\u0149\u0148\3\2\2\2\u014a\u014d\3\2\2\2\u014b"+
		"\u0149\3\2\2\2\u014b\u014c\3\2\2\2\u014c\u014e\3\2\2\2\u014d\u014b\3\2"+
		"\2\2\u014e\u014f\b\67\2\2\u014fn\3\2\2\2\u0150\u0151\7\61\2\2\u0151\u0152"+
		"\7,\2\2\u0152\u0156\3\2\2\2\u0153\u0155\13\2\2\2\u0154\u0153\3\2\2\2\u0155"+
		"\u0158\3\2\2\2\u0156\u0157\3\2\2\2\u0156\u0154\3\2\2\2\u0157\u0159\3\2"+
		"\2\2\u0158\u0156\3\2\2\2\u0159\u015a\7,\2\2\u015a\u015b\7\61\2\2\u015b"+
		"\u015c\3\2\2\2\u015c\u015d\b8\2\2\u015dp\3\2\2\2\u015e\u0160\t\7\2\2\u015f"+
		"\u015e\3\2\2\2\u0160\u0161\3\2\2\2\u0161\u015f\3\2\2\2\u0161\u0162\3\2"+
		"\2\2\u0162\u0163\3\2\2\2\u0163\u0164\b9\2\2\u0164r\3\2\2\2\n\2\u012b\u0134"+
		"\u013c\u013f\u014b\u0156\u0161\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}