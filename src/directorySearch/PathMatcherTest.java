package directorySearch;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class PathMatcherTest {
	public static void main(String[] args) throws IOException {
		
		//O que e um PathMatcher? E basicamente um regex dos Paths, usa-se quando voce quer validar ou procurar um Path
		
		//Antes de comecar, tenho que esclarecer que no Windows e no Linux normalmente sao usados caracteres diferentes
		//Windows: \ (Contra-barra); Linux: / (Barra :D). Mas o java consegue fazer que o Windows perceba que e um diretorio
		//Usando a barra normal, entao para instanciar um Path, usa o seprador de uma barra normal, ou separe pelo metodo
		//Usando virgulas
		
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob: imagineUmGlobAqui");
		
		//Antes de comecar a usar glob (glob e o nome do "regex" do PathMatcher), vou explicar a linha de codigo acima
		//Primeiramente usamos o FileSystems.getDefault(), para o java conseguir acesso ao file systems (eu nao entendi
		//Muito bem essa parte), citacao do java:
		//"The default file system creates objects that provide access to the file systems accessible to the Java virtual machine"
		//E o getPathMatcher para criar um glob.
		
		Path path = Paths.get(System.getProperty("user.home"), "Desktop", "Pasta", "Arquivo.java");
		Path arquivo2 = Paths.get("Arquivo2.java");
		Path arquivo3 = Paths.get("Arquivo3.java");
		Path arquivo4 = Paths.get("Arquivo.exe");
		Path arquivo5 = Paths.get("Arquivo2.exe");
		Path arquivo6 = Paths.get("Arquivo3.exe");
		Path arquivo7 = Paths.get("Arquivo3.txt");
		
		if(Files.notExists(path.getParent())) {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
			Files.createFile(path.getParent().resolve(arquivo2));
			Files.createFile(path.getParent().resolve(arquivo3));
			Files.createFile(path.getParent().resolve(arquivo4));
			Files.createFile(path.getParent().resolve(arquivo5));
			Files.createFile(path.getParent().resolve(arquivo6));
			Files.createFile(path.getParent().resolve(arquivo7));
		}
		
		//Matcher com caminhos relativos...
		
		//Fazendo com caminhos relativos, porque e mais facil pra explicar
		
		matcher = FileSystems.getDefault().getPathMatcher("glob:*.exe");
		
		System.out.println("Arquivo java: " + matcher.matches(arquivo2));
		System.out.println("Arquivo exe: " + matcher.matches(arquivo4));
		
		//Depois dos dois pontos e aonde esta o glob, "*.exe" o ateristico ele basicamente aceita qualquer coisa
		
		//Agrupamento...
		
		//Para a gente aceitar mais de uma coisa, colocamos dentro de chaves
		matcher = FileSystems.getDefault().getPathMatcher("glob:*.{exe,java}");
		
		System.out.println("Arquivo java: " + matcher.matches(arquivo2));
		System.out.println("Arquivo exe: " + matcher.matches(arquivo4));
		
		//Aqui a gente aceita exe e java
		
		//Caracteres indefinidos...
		
		matcher = FileSystems.getDefault().getPathMatcher("glob:*.{java,???}");
		//Podemos colocar integorracoes para simbolizar caracteres, cada interrogacao e um caractere
		
		System.out.println("Arquivo java: " + matcher.matches(arquivo2));
		System.out.println("Arquivo java: " + matcher.matches(arquivo7));
		
		//Matcher com paths absolutos
		
		path = Paths.get(System.getProperty("user.home"), "Desktop", "Pasta", "Arquivo.java");

		matcher = FileSystems.getDefault().getPathMatcher("glob:*.java");
		System.out.println("Path Absoluto: " + matcher.matches(path));
		
		//Por que nao funcionou? Porque um asteristico significa que ele aceita qualquer coisa, mas ele nao reconhece
		//Separacao de path, ou seja, so usamos um asteristico se for um path relativo, para funcionar com absoluto
		//Usamos dois asteristico.

		matcher = FileSystems.getDefault().getPathMatcher("glob:**.java");
		System.out.println("Path Absoluto: " + matcher.matches(path));
		
		//Como garintir que e um path absoluto e nao um relativo...
		
		//Como voce sabe, os dois asteristicos aceita qualquer coisa e tambem reconhece o separador de diretorio,
		//Mas entao ela tambem vai aceitar relativo ne? Ela aceita qualquer coisa, entao sim. E como prevenir isso?
		
		matcher = FileSystems.getDefault().getPathMatcher("glob:**.java");
		System.out.println("Test 2 Asteristicos: " + matcher.matches(arquivo2));
		//Como voce pode ver ele aceita relativo
		
		matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java");
		System.out.println("Path Relativo: " + matcher.matches(arquivo2));
		//Por que deu erro? Porque ele aceita qualquer coisa, mas no final, vai ter que ter uma barra, ele vai aceitar qualquer
		//Nome depois da barra, e depois tem que ter o ".java", ou seja, ele so vai aceitar Paths, que tenham no minimo
		//Dois caminhos

		matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.java");
		System.out.println("Path Absoluto: " + matcher.matches(path));
		
		//Mas tambem podemos usar Regex no PathMatcher, usando o "regex:" antes.
		matcher = FileSystems.getDefault().getPathMatcher("regex:(.){0,}\\.java");
		System.out.println("Path Absoluto (Regex): " + matcher.matches(path));
		//Como voce sabe o "." em regex aceita qualquer coisa
		
		//Teste com SimpleFileVisitor
		
		System.out.println("\nuser.dir = " + System.getProperty("user.dir") + "\n");
		Files.walkFileTree(Paths.get(System.getProperty("user.dir")), 
				new findAllTest());
		
		//"user.dir" retorna o path do projeto atual atual da classe.
		
		//Caracteres especiais do glob...
		
		// * - Aceita qualquer coisa, mas nao reconhece separadores de Path.
		// ** - Aceita qualquer coisa e reconhece separadores de Path.
		// ? - Procura por um unico caractere qualquer.
		// [] - Agrupamento. E literalmente igual ao regex. Ex.: [abc0-9].
		//Mas claro, nao podemos usar os caracteres especiais do regex, como o \\s, \\d, \\w e etc.
		//Mas podemos usar regex no PathMatcher usando o "regex:" antes.
		
	}
}

class findAllTest extends SimpleFileVisitor<Path>{
	
	private PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*Test*.{java,class}");
	//Precisamos garantir que seja paths absolutos.
	
	private int contador = 1;
	
	@Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		
		if(matcher.matches(file)) { //Ou seja, ele so vai mostrar as classes ou .java que tem o nome Test nela
			System.out.println(contador + "º - " + file.getFileName());
			contador++;
		}
		
		return FileVisitResult.CONTINUE;
	}
}
