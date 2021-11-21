package fileNIO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PathNIO {
	public static void main(String[] args) {
		
		//Primeira coisa que eu vo falar aqui e que "Path" e uma interface e "Paths" e uma classe, e o que isso impacta no
		//Codigo? A gente vai instaciar a interface "Path" porque ai a gente vai conseguir, tanto usar metodos da classe
		//"Paths" e "Files".
		
		//Instanciando uma Path...
		System.getProperty("user.home");
		Path caminhoDesktop = Paths.get("C:\\Users\\Test\\Desktop");//Aqui voce consegue ver que criamos uma variavel
		//Que referencia a interface Path, e depois usamos um metodo do Paths para pegar o caminnho. Ate aqui tudo bem
		//Praticamente igual ao antigo, mas, podemos fazer isso...
		
		Path caminhoDesktop2 = Paths.get("C:", "Users", "Test\\Desktop");//Podemos tirar as contrabarras e substituir
		//Por virgulas. Talvez isso seja util ._. Depende muito do caso
		
		System.out.println(caminhoDesktop2.toAbsolutePath());//Como podemos ver, funciona sem problemas
		
		//Convertendo...
		
		File file = caminhoDesktop.toFile();
		System.out.println(file.getAbsolutePath());

		Path path = file.toPath();
		System.out.println(path.toAbsolutePath());
		
		//Como podemos ver, podemos converter de Path pra File e vice-versa
		
		//Ok. Mas como cria diretorios com Path? Assim...
		
		Path pasta = Paths.get("C:\\Users\\Test\\Desktop", "Pasta Loca");
		
		try {
			//Como voce ja sabe, se agente tentar criar uma pasta com o nome ja existente, ele vai retornar false, com isso
			//Podemos fazer isso...
			if(Files.notExists(pasta)) {
				System.out.println("Pasta Criada? " + Files.createDirectory(pasta));
			} else {
				System.out.println("Já Existe Pora");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Ok, mas se eu quiser cirar diversas pastas? Vou ter que instanciar um Path pra cada pasta? Nao meu amigo, o NIO
		//Ja pensou nisso
		
		Path variasPastas = Paths.get("C:\\Users\\Test\\Desktop", "Pasta loca", "Pasta Normal", "Pasta");//Posso colocar
		//Pastas em uma pasta que acabei de cirar
		
		//Primeiramente criamos um Path com todas as pastas que queremos
		
		try {
			if(Files.notExists(variasPastas)) {
				System.out.println("Pastas Criadas? " + Files.createDirectories(variasPastas));//So usar o metodo 
				//createDirectories, no plural.
				
		} else {
			System.out.println("Já Existem Pora");
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
		
		//Ok, mas como eu faco pra criar um arquivo? Criando um Path tambem, olhe...
		
		Path arquivo = Paths.get("C:\\Users\\Test\\Desktop", "Pasta loca", "Pasta Normal", "Pasta", "arquivo.txt");
		
		try {
			if(Files.notExists(arquivo)) {
				System.out.println("Pastas Criadas? " + Files.createDirectories(arquivo)); //Se a gente fazer isso, ele vai criar
				//Uma pasta. Olha la
				//Temos que usar outro comando para crair um arquivo
				//Entao como eu faco pra criar varias pastas e no final criar um arquivo?
				
		} else {
			System.out.println("Já Existem Pora");
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
		
		//Criando...
		
		//Temos que renomear
		
		arquivo = Paths.get("C:\\Users\\Test\\Desktop", "Pasta loca", "Pasta Normal", "Pasta", "arquivoDeVerdade.txt");
		
		try {
			if(Files.notExists(arquivo)) {
				System.out.println("Pastas Criadas? " + Files.createFile(arquivo));//Usamos o metodo createFile(), se o
				//Diretorio que a gente botou nao existir, ele vai dar erro
				
		} else {
			System.out.println("Já Existem Pora");
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
		
		//Ok, mas como eu crio as pastas e tambem um arquivo com o mesmo Path? Assim...
		
		Path arquivoEPasta = Paths.get("C:\\Users\\Test\\Desktop", "Pasta", "Pasta2", "arquivo.txt");
		
		try {
			if(Files.notExists(arquivoEPasta.getParent())) {//O que e o getParent()? Ele simplesmente vai excluir o ultimo
				//Caminho do Path, ou seja, ele so vai ate a Pasta2 e vai parar
				System.out.println("Pastas Criadas? " + Files.createDirectories(arquivoEPasta.getParent()));//Aqui tambem
				System.out.println("Pastas Criadas? " + Files.createFile(arquivoEPasta));//Depois e so criar normal
				
				//Ou seja, criamos varias pastas, e um arquivo sem precisar criar outro Path ou trocar seu caminho
				
		} else {
			System.out.println("Já Existem Pora");
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
		
		//Copiando um arquivo...
		
		Path arquivoCopiado = Paths.get("C:\\Users\\Test\\Desktop", "PastaCopia", "arquivo2.txt");//Aqui vou criar
		//Outra pasta para criar um "arquivo2.txt" e copiar o "arquivo.txt" dentro do 2, ou seja, podemos copiar o 
		//Arquivo e tambem renomea-lo.
		//Obs.: Tudo que estivor no arquivo.txt vai ser copiado pro arquivo2.txt, ou seja, se tiver algo escrito em um
		//Vai copiar pro outro
		
		//Vou escrever algo so pra voce ver...
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoEPasta.toString()))) {//Temos que mandar
			//.toString() porque ele so aceita arquivos File, ou Strings.
			bw.write("Quem me copiar é gay");
			bw.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Vou criar uma pastinha, para copiar o arquivo la
		try {
			if(Files.notExists(arquivoCopiado.getParent())) {
				Files.createDirectories(arquivoCopiado.getParent());
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		try {
			Files.copy(arquivoEPasta, arquivoCopiado);//O primeiro parametro e a "source" ou seja, o arquivo que vamos
			//Copiar, e o segundo parametro e o arquivo que vamos usar para colar a source dentro
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Mas o codigo de cima da pra considerar meio ruim, porque podemos adicionar mais um parametro no Files.copy
		//Que se existir um arquivo com o mesmo nome, ele vai substituir
		
		try {
			Files.copy(arquivoEPasta, arquivoCopiado, StandardCopyOption.REPLACE_EXISTING);//Aqui usamos uma Enum
			//E usamos o REPLACE_EXISTING, caso ja exista um arquivo ele substituir
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Deletando um arquivo...
		
		//Existem dois metodos pra isso, mas voce so vai usar 1, porque um so deleta se existir e se voce usar o outro
		//E tentar deletar algo que nao existe da erro, entao, como a gente nunca tem certeza de nada sempre use
		//o deleteIfExists().
		
		try {
			Files.deleteIfExists(arquivoCopiado);//E aqui passamos o Path do arquivo
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Uma coisa interessente...
		
		//O Files ja tem um metodo para criar BufferedWriter e BufferedReader sem ter que escrever uma linha de codigo
		//Imensa
		
		try {
			Path arquivoImaginario = Paths.get("ArquivoImaginario.txt");
			
			//Normal...
			BufferedWriter normal = new BufferedWriter(new FileWriter(arquivoImaginario.toString()));
			
			//Com Files
			BufferedWriter filesWriter = Files.newBufferedWriter(arquivoImaginario);//Nem precisamos usar o toString() 
			//Porque ele aceita Path, entao se estiver trabalhando com path e precisar usar Writer, use o Files
			
			System.out.println(filesWriter.getClass());
			System.out.println(normal.getClass());
			//Como podemos ver, eles sao a mesma coisa
			
			//Mesma coisa com o Reader...
			BufferedReader filesReader = Files.newBufferedReader(arquivoImaginario);
			
			System.out.println(filesReader.getClass());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}