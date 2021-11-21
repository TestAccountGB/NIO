package directorySearch;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryStreamTest {
	public static void main(String[] args) {
		
		//Essa e uma classe bem simples de pesquisa, mas e bom ter classes simples, para pesquisa simples :)
		
		Path directory = Paths.get(System.getProperty("user.home"), "Desktop", "DirectorySearch", "SubPasta", "ArquivoTop.txt");
		Path directory2 = Paths.get(System.getProperty("user.home"), "Desktop", "DirectorySearch", "Arquivo.txt");
		
		try {
			if(Files.notExists(directory.getParent())) {
				
				Files.createDirectories(directory.getParent());
				Files.createFile(directory);
				Files.createFile(directory2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory2.getParent())){//TryWithResources
			//Temos que instanciar usando apenas a pasta, por isso estou usando o getParent().
			//Aqui usamos a mesma logica que o ArrayList, usamos a classe do diretorio que estamos usando. Pode ser um 
			//Path ou um File, e depois a classe Files para instanciar o DirectoryStream
			
			for(Path path : stream) { //Foreach...
				
				System.out.println(path.getFileName());//O path ja tem um metodo de pegar o nome do arquivo, mas ele so
				//Pega o nome do arquivo que lhe foi escrito no comeco.
				
				//Mas como podemos ver, a gente tem um arquivo em "SubPasta", mas essa classe e bem simples, e nao faz
				//Essa pesquisa entrando em pastas.
			}
			
			System.out.println("\nTeste: " + directory.getFileName());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Basicamente e isso que a classe DirectoryStream faz, mas ai se voce viu o package IO a gente tem um metodo que
		//Faz exatamente isso, entao por que usar uma classe pra fazer isso? Primeiramente, a classe Path nao tem esse
		//Metodo. Segundamente, a classe DirectoryStream consegue fazer filtracoes, mas ainda sim, e muito simples
		//So consegue fazer isso, mas claro, se quiser fazer pesquisas simples, utilize ela.
		
	}
}