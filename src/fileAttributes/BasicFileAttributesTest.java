package fileAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class BasicFileAttributesTest {
	public static void main(String[] args) throws IOException {
		
		 File file = new File(System.getProperty("user.home")+"\\Desktop\\BasicFileAttributesTest.txt");
		
		Date date = new GregorianCalendar(2015, Calendar.JANUARY, 21).getTime();//O calendario gregoriano, é o que a
		//Gente usa. Como voce pode ver, podemos usar o Calendar para falar o mes.
		
		file.createNewFile();//Estou botando o throws no main, apenas por preguica de fazer um try and catch ._.
		
		file.setLastModified(date.getTime());//Quando a gente usa o getTime() em um date, ele retorna um long, que e
		//O que esse metodo precisa, vamos setar seu atributo de ultima vez modificado.
		
		System.out.println(file.lastModified());//Como podemos ver, ele retorna o valor em long, e como o construtor do
		//Date aceita long, podemos fazer isso...
		
		System.out.println(new Date (file.lastModified()));//Agora sim, podemos ver que conseguimos alterar seu atributo
		
		file.delete();
		
		//E assim que alteramos atributos da classe File, se quiser fazer mais testes com outros atributos, e so escrever
		//file.set que vai aparecer varios
		
		//Mas como alteramos na classe NIO?
		
		 Path path = Paths.get(System.getProperty("user.home"), "Desktop", "BasicFileAttributesTest.txt");
		 Files.createFile(path);
		 
		 FileTime filetime = FileTime.fromMillis(date.getTime()); //Aqui precisamos criar um FileTime, que instaciamos
		 //Com milissegundos, igual o de cima
		 
		 //E aqui usamos um metodo da classe File, como primeiro parametro, o arquivo que quer alterar, e o segundo
		 //O tempo.
		 Files.setLastModifiedTime(path, filetime);
		
		System.out.println(Files.getLastModifiedTime(path));//E para pegar a data, podemos usar um metodo da classe Files
		
		//Ok, mas a gente nao para por ai, a gente tem uma classe especializada pra isso no NIO
		
		//BasicFileAttributes...
		
		//Com ele podemos ver nossos atributos basicos, se chamam basicos porque tem em todos os Sistemas Operacionais
		
		BasicFileAttributes atributosBasico = Files.readAttributes(path, BasicFileAttributes.class);
		//Aqui como podemos ver para instancia-lo usamos um metodo readAttributes() da classe Files, porque vamos
		//Apenas ler e nao mudar nada. O BasicFileAttributes.class, significa basicamente que a gente esta se referenciando
		//A aquela classe
		
		//Um exemplo para explicar melhor...
		
		//BasicFileAttributes atributosBasicoAux = Files.readAttributes(path, atributosBasico.getClass());
		
		//Como podemos ver nao tem nenhum erro, mas vai dar um erro de excecao em tempo de execucao, mas por que?
		//Porque o metodo nao esta preparado para receber desse jeito, mas basicamente a gente o usa o .class, quando
		//A gente nao tem nenhuma classe para referenciar a classe.
		
		System.out.println("Data de Criacao: " + atributosBasico.creationTime());//Ele retorna um FileTime
		System.out.println("Last Access: " + atributosBasico.lastAccessTime());
		System.out.println("Last Modified: " + atributosBasico.lastModifiedTime());
		
		//A gente tem diversos metodos nessa classe, tudo relacionado a visualizacao dos atributos de um arquivo, alguns
		//Metodos podem ser bem especificos, voce pode apertar ctrl + botao esquerdo, para saber o que ele faz, o java
		//Explica muito bem .-.
		
		//Obs.: Se quiser ver algo simples, como sua ultima vez modificado, nao precisa instanciar um BasicFileAttributes.
		
		//E agora para modificar os atributos como fazemos?
		
		//Falo dnv, se quiser fazer algo simples, nao instancie essas classes, so vai ser mais peso pro seu codigo...
		
		//Mas caso queria modificar algo que nao esteja no Files, podemos usar o BasicFileAttributeView, que estranho ne?
		//A classe pra modificar tem View no nome, mo nada a ver isso ai .-.
		
		//Mesmo esquema para instanciar...
		
		BasicFileAttributeView modificadorAtributosBasico = Files.getFileAttributeView(path, BasicFileAttributeView.class);
		//Como podemos ver, ele usa agora o metodo getFileAttributeView.
		
		//Como estamos usando o modificador basico, so vamos poder mudar algumas datas, entao vamos precisar dos
		//FileTime
		
		Calendar c = Calendar.getInstance();
		c.set(2018, Calendar.APRIL, 23);
		FileTime lastModified = FileTime.fromMillis(c.getTimeInMillis());//Calendar como e incrivel, ele ja tem um metodo
		//Que retorna em milissegundos
		
		c.set(2020, Calendar.DECEMBER, 2);
		FileTime lastAccess = FileTime.fromMillis(c.getTimeInMillis());

		c.set(2015, Calendar.JANUARY, 5);
		FileTime creation = FileTime.fromMillis(c.getTimeInMillis());
		
		modificadorAtributosBasico.setTimes(lastModified, lastAccess, creation);//Infelizmente tem que ser nessa ordem
		// lastModified -> lastAccess -> creation
		
		//Temos que atualizar...
		atributosBasico = Files.readAttributes(path, BasicFileAttributes.class);
		
		//E como podemos ver... Funfou :D
		System.out.println("\nCriação: " + atributosBasico.creationTime());
		System.out.println("Last Modified: " + atributosBasico.lastModifiedTime());
		System.out.println("Last Access: " + atributosBasico.lastAccessTime());
		
		//Mas po. E so isso que da pra mecher? Nao, como eu falei, estamos mechendo com apenas o basico, temos diversos
		//Tipos de atributos, vou passar rapidinho sobre eles...
		
		//DosFileAttributeView
		
		//Ele e a mesma coisa que o basic, mas tem mais funcoes voltadas pro Windows.
		
		DosFileAttributeView dosView = Files.getFileAttributeView(path, DosFileAttributeView.class);
		
		//Temos umas funcoes legais como
		
		dosView.setHidden(true);//Agora o arquivo vai esta oculto :O
		
		//Caso a gente nao queira criar um objeto, podemos fazer isso...
		
		Files.setAttribute(path, "dos:hidden", true);
		Files.setAttribute(path, "dos:readonly", true);
		dosView.setReadOnly(false);//Temos que tirar, porque depois nao vou conseguir apagar ou modificar :0
		
		//Mas para ver esses alteracoes, ainda precisamos criar um DosFileAttributes. Mas claro que usando o objeto
		//Fica muito mais organizado e facil de entender.
		
		
		//Se quiser aprender mais sobre ele, va ate sua documentacao e leia
		
		//PosixFileAttributeView
		
		//Focado no Sistema Operacional Unix, como o Linux
		
		@SuppressWarnings("unused")
		PosixFileAttributeView posixView = Files.getFileAttributeView(path, PosixFileAttributeView.class);
		
		//Tambem temos uma funcoes legais. Mas eu nao sei usar :(
		//posixView.setPermissions(null);
		
		//Mas e focado em linux, entao fds :)
		
		//FileOwnerAttributeView
		
		//Focado nas permicoes de dono do arquivo, como o nome ja diz ._.
		
		FileOwnerAttributeView ownerView = Files.getFileAttributeView(path, FileOwnerAttributeView.class);
		
		System.out.println(ownerView.getOwner());
		
		//AclFileAttributeView
		
		//Focado em mudar as permissoes dos arquivos
		
		AclFileAttributeView aclView = Files.getFileAttributeView(path, AclFileAttributeView.class);
		
		System.out.println(aclView.getAcl());
		//Muito avancado pra mim :(
		
		//Acabamos... Mas e claro que cada um tem sua classe para olhar os atributos. Vou dar um exemplo
		
		DosFileAttributes dosAtributosBasico = Files.readAttributes(path, DosFileAttributes.class);
		
		System.out.println(dosAtributosBasico.isHidden());
		
		Files.deleteIfExists(path);
		
		
		
		
		//Como podemos ver e muito vantajoso usar a classe Files, pela sua flexibilidade e quantidade de funcoes que
		//Podem ser uteis em determinadas situacoes, mas, apenas use ela nesses determinadas situacoes. Normalmente
		//Um simples File do IO resolvera o problema.
		
		
	}
}