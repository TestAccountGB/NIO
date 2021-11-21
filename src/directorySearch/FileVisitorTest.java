package directorySearch;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileVisitorTest {
	
	
	public static void main(String[] args) {
		
		Path path = Paths.get(System.getProperty("user.home"), "Desktop", "PastaLegal", "PastaMaisLegal", "Arquivo.exe");
		Path path2 = Paths.get(System.getProperty("user.home"), "Desktop", "PastaLegal", "ArquivoDaora.txt");
		
		//Criando pastas e arquivos para teste
		
		try {
			if(Files.notExists(path2.getParent())) {
				Files.createDirectories(path.getParent());
				Files.createFile(path);
				Files.createFile(path2);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			//Na classe file temos um metodo chamado walkFileTree, que como o nome diz, ele vai andar pela arvore do
			//Arquivo, ou seja, ele vai pecorrer tudo, todas as pastas e etc. Mas para usar ele, precisamos de um
			//SimpleFileVisitor, mas, como o java e topzinho, podemos fazer nosso proprio personalizado, e para isso, vou
			//Fazer uma classe local, mas claro, voce pode separar e deixar mais organizadinho, mas assim vai ficar melhor
			//Para explicar
			
			Files.walkFileTree(Paths.get(System.getProperty("user.home"), "Desktop"), 
					new AcharTodosExecutaveis());
			
			//Como voce pode ver, a gente so passou um Path do desktop, mas como a gente passou o FileVisitResult com
			//CONTINUE, ele vai continuar e pecorrer todas as pastas do Path, ou seja, ele foi em TODAS as pastas do Desktop,
			//Mas so mostrou um arquivo por causa da nossa vereficacao.
			
			//Obs.: Observe que a gente nao mandou ele executar o metodo visitFile(), mas o Files.walkFileTree, ja faz isso
			//Automaticamente pra gente, ele vai executar a classe SimpleFileVisitor inteira, ou seja, ele vai executar
			//O preVisitDirectory, visitFile, visitFileFailed e o postVisitDirectory. E como a gente sobrescreveu
			//O visitFile, ele vai executar o nosso visitFile. Observe o comando a seguir...
			
			System.out.println("\nMostrando tudo da pasta \"PastaLegal\"\n");
			
			Files.walkFileTree(Paths.get(System.getProperty("user.home"), "Desktop", "PastaLegal"), 
					new MostrarTudo());
			
			//Como voce pode ver ele executa primeiramente o preVisitDirectory para entrar nas pastas, e quando acha um
			//Arquivo ele usa o visitFile, quando da erro ele usa o visitFileFailed e para voltar as pastas ele usa o
			//postVisitDirectory.
			
			//Se quiser entender mais a fundo como funciona os metodos, tire o "PastaLegal" do final e analise o console
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}

//Classe Local...

//Obs.: Tem que estar fora da classe principal, obviamente.

class AcharTodosExecutaveis extends SimpleFileVisitor<Path>{//Temos que extender o SimpleFileVisitor e colocar
	//Dentro dos sinais de menor e maiores a classe que queremos usar.
	
	//Dentro do SimpleFileVisitor temos o metodo visitFile, que vai funcionar junto com o walkFileTree do Files,
	//Que ele basicamente precisa de um FileVisitResult que e uma enumaracao, que retorna o tipo de pesquisa que
	//Queremos, como por exemplo o CONTINUE, que ele basicamente entra em todos os diretorios para a pesquisa.
	
	@Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		
		//E no metodo podemos escrever a logica que queremos.
		
		//Como por exemplo, retornar todos os executaveis das pastas e subpastas.
		
		if(file.getFileName().toString().endsWith(".exe")) {//Ou seja, nossa logica significa que vamso pegar o Path que
			//A pessoa passar, e depois vamos pegar o nome da pasta, e passar para uma String, porque o metodo
			//getFileName retorna um Path, ou seja, precisamos transformar em String para comparar com o metodo
			//endsWith, que nele escrevemos o que queremos que o file termine.
			
			//E depois dessa vereficacao ele vai executar o codigo a seguir...
			
			System.out.println(file.getFileName());//O metodo walkFileTree ja vai pecorrer toda a pasta, ou seja, nao
			//Precisamos nos preocupar com isso. Aqui so vamos escrever pra ele mostrar o nome do arquivo, porque
			//Os arquivos que passarem pela vereficacao, vao ser os que terminam com "exe".
			
		}
		
		//System.out.println(file.getFileName()); //Tire de comentario esse codigo, caso queira ver todos os arquivos
		//Sem passar pela vereficacao, mas claro, coloque o if inteiro em comentario!
		
		//E mesmo se nao passar pela vereficacao precisamos retornar o FileVisitResult, que se a vereficacao for falsa
		//Ele vai continuar e pesquisar outro arquivo.
		
        return FileVisitResult.CONTINUE;
    }
}

class MostrarTudo extends SimpleFileVisitor<Path>{
	
	 @Override
	    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException{
		 
		 System.out.println("Entrando na Pasta - " + "\"" + dir.getFileName() + "\"");
		 
		 return FileVisitResult.CONTINUE;
	 }
	
	 @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
		 
		 System.out.println("Mostrando o Arquivo - " + "\"" + file + "\"");
		 
		 return FileVisitResult.CONTINUE;
	 }
	 
	 @Override
	    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		 
		 System.out.println("Deu Erro Porra");
		 throw exc;
	 }
	 
	 @Override
	    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		 
		 System.out.println("Voltando uma Pasta - " + "\"" + dir.getFileName() + "\"");
		 
		 return FileVisitResult.CONTINUE;
	 }
	 
	//Obs.: Se quiser, pode tirar o getFileName() do final, que vai mostrar todo o diretorio
}

//Tipos de FileVisitResult...

//CONTINUE - Ele vai continuar executando.

//TERMINATE - Ele vai terminar o codigo do SimpleFileVisitor.

//SKIP_SIBLINGS - Ele vai continuar, mas nao vai entrar nas pastas do mesmo nivel, ou seja, na pasta Dekstop, temos
//A pasta "Documento", "Downloads" etc. No mesmo nivel de Desktop, ou seja, ele vai continuar mas nao vai entrar
//Nessas pastas.

//SKIP_SUBTREE - Ele vai continuar, mas nao vai entrar nas pastas filhas, ou seja, na pasta Desktop, ele vai poder entrar
//Na pasta "Documento", "Downloads" e etc. Mas nao vai entrar nas pastas que estao dentro de Desktop.

