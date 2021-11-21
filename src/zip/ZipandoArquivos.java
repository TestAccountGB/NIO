package zip;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipandoArquivos {
	public static void main(String[] args) {
		
		//Fazendo pastas e arquivo para o processo de compressao...
		
		Path pastaNormal = Paths.get(System.getProperty("user.home"), "Desktop", "PastaTestZip", "PastaNormal");
		Path arquivoTxt = pastaNormal.resolve("Arquivo.txt");
		Path arquivoExe = pastaNormal.resolve("Arquivo.exe");
		
		//Criando as pastas e os arquivos, e escrevendo nele, para testar se o zip consegue comprimir sem mecher no
		//Conteudo dentro
		if(Files.notExists(pastaNormal)) {
			try {
				Files.createDirectories(pastaNormal);
				Files.createFile(arquivoTxt);
				Files.createFile(arquivoExe);
				
				//Nao podemos usar o BufferedWriter com try-with-resources, porque a gente tem que instanciar ele ja com o
				//Arquivo criado, por causa disso precisamos fechar ele depois de escrever.
				BufferedWriter bw = Files.newBufferedWriter(arquivoTxt);
				bw.write("Teste zip kkkkk");
				bw.newLine();
				bw.write("Test New Line");
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Fazendo o path aonde a pasta zipada vai ficar
		Path pastaZipada = pastaNormal.getParent().resolve("PastaZipada.zip");
		
		//Obs.: Se voce nao viu Input e Output Stream da classe IO, vai olhar porra
		
		//Como a gente sabe o Input e Output trabalha com bytes, e o que a gente vai fazer, mas agora com um Output
		//Especializado para arquivos Zip...
		
		try(ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(pastaZipada.toFile()))){
			
			//Como a gente nao tem subpastas para mecher vou usar o DirectoryStream ao inves do FileVisitor, ja que a gente
			//Ta mechendo com pastas e arquivos simples...
			
			DirectoryStream<Path> stream = Files.newDirectoryStream(pastaNormal);//Pasta Normal e aonde tem os arquivos
			//A serem comprimidos
			
			for(Path path : stream) {
				
				ZipEntry entradaZip = new ZipEntry(path.getFileName().toString());
				//Primeiramente, o que o ZipEntry? Estamos criando ele, porque ele consegue trabalhar com o
				//ZipOutputStream. Segundamente, que pora ta acontecendo aqui? A cada arquivo que o DirectoryStream
				//Achar, ele vai mandar pra esse foreach, e a variavel do foreach vai criar um ZipEntry a cada ciclo,
				//Porque precisamos crair um ZipEntry para cada arquivo, e tambem porque no contrutor do ZipEntry e esperado
				//Uma String com o NOME (Nao e o Path, apenas o nome) do arquivo, por isso a gente coloca o .getFileName 
				//E o .toString no final para transformar o mesmo em String
				
				zip.putNextEntry(entradaZip);
				//Aqui estamos usando um metodo da classe ZipOutputStream em que ele vai criar um arquivo dentro da
				//"pastaZipada", mas nao vai ter nada dentro, ele so vai criar.
				
				//Mas podemos diminuir o codigo assim:
				//zip.putNextEntry(new ZipEntry(path.getFileName().toString()));
				//Assim economizamos linha e possivelmente memoria, pois ele nao cria uma variavel, entao use assim :D, 
				//Nao estou usando porque fica pior pra explicar
				
				//Obs.: Se a gente parar o codigo aqui e executa-lo, ele so vai criar uma pasta zip, com os nomes e extensoes
				//Dos arquivos da "PastaNormal", mas com nada dentro, mas pelo menos, da pra perceber que a logica esta
				//Dando certo :D
				
				//O que a gente precisa fazer agora, e copiar cada arquivo que o DirectoryStream manda pra gente, para dentro
				//Dos arquivos da "PastaZipada", para que ai sim, ter alguma coisa dentro
				
				//Agora vamos crair um Reader de Bytes para que pegue o que esta dentro do arquivo
				
				BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path.toFile()));
				//Criacao de um BufferedInputStream normal, usando a variavel do foreach
				
				//Agora falta criar um array de bytes para colocar os bytes lidos dentro dele
				
				byte[]  bytesLidos = new byte[30000];//Tenta fazer alguns testes de tempo com o System.currentTimeMillis()
				//Botando alguns numeros aqui, como 50k, 100k etc e tambem testa com numeros pequenos como 500, 1000
				
				int quantidadeDeBytes;
				
				while ((quantidadeDeBytes = bufferedInputStream.read(bytesLidos)) != -1) {//O while ja expliquei no projeto
					//do IO e como ele funciona e por que usamos o -1.
					
					zip.write(bytesLidos, 0, quantidadeDeBytes);//Aqui usamos o objeto zip que instanciamos la no comeco.
					//O primeiro parametro e o que ele vai escrever, ou seja, nosso array de byes que o .read mandou pra ele
					//O segundo parametro e aonde ele vai comecar, ou seja, o indice, e o terceiro parametro e o tamanho.
					//E bom a gente colocar o tamanho, pois o java pode alocar um tamanho maior que o necessario.
					System.out.println(quantidadeDeBytes);
				}
				
				zip.flush(); //Flush pra garantir. Obs.: Coloque o flush fora do while. E pronto :D Agora e so fechar os objetos
				//E acabamos
				zip.closeEntry(); //Nao precisamos usar o zip.close() porque ele esta no try-with-resources
				bufferedInputStream.close();
			}
			
			//E como podemos ver, agora o arquivo do zip esta com tudo escrito normalmente xD
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 //Obs.: Funciona com qualquer tipo de arquivo. Executavel .sql, .dll TUDO. Pois a gente esta comprimindo byte
		//Por byte. Testei com um arquivo de 100mb e foi pra 20mb, ou seja, esta realmente funcionando :D. Mas arquivos
		//Como de musica e imagens ele nao consegue diminuir, porque ai envolve um monte de putaria, mas pelo menos
		//Da pra colocar tudo em um so .zip
		
		//Obs.: A gente nao esta trabalhando com subpastas, entao, se passar diversas pastas dentro de uma pasta
		//Para comprimir, nao ira funcionar.
		
		//Se quiser testar alguns arquivos, primeiramente rode o codigo uma vez pra ele criar as pastas, depois e so colocar
		//O arquivo que voce quer testar dentro da pasta "pastaNormal" e obviamente rodar o codigo novamente.
		
		
	}
}
