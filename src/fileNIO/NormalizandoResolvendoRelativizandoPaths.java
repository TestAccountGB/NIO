package fileNIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NormalizandoResolvendoRelativizandoPaths {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		//Como voce e uma pessoa inteligente e sabia, voce sabe que para voltar em um terminal usamos o comando
		//"cd .." ou "cd ..\", e algumas vezes podemos pegar algum script que esteja assim...
		//C:\Users\Test\Documents\..
		//Porque alguem vai deixar o script assim? Sla porra
		
		//Normalizando Paths
		
		System.out.println("Normalizando Paths\n");
		
		Path caminho = Paths.get("C:\\Users\\Test\\Documents\\..\\Arquivo.txt"); //Aqui a gente ta criando um Path em
		// C:\Users\Test\Arquivo.txt porque a gente ta voltando com os pontos
		
		//Testando
		
		try {
			if(Files.notExists(caminho)) {
				Files.createFile(caminho);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Files.deleteIfExists(caminho);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Se quiser apagar tira o codigo do comentario
		
		System.out.println(caminho);//Se a gente quiser mostrar o caminho ou colocar o caminho em uma string,
		//Podemos usar o normalize() que ele vai voltar pra gente.
		
		//Usando o normalize()
		System.out.println(caminho.normalize());
		
		//Colocando em uma String...
		String string = caminho.normalize().toString();
		System.out.println(string);
		
		//Depois podemos colocar no mesmo Path...
		caminho = Paths.get(string);
		//Mas nao recomendo fazer isso, porque e basicamente uma gambiarra
		
		//Tambem tem o comando "cd ." que referencia ao mesmo caminho, a gente usa ele para criar arquivos no Path que
		//A gente esta
		
		Path caminho2 = Paths.get("C:\\Users\\Test\\Documents\\.\\.\\.\\.\\Arquivo.txt");
		
		System.out.println(caminho2);
		System.out.println(caminho2.normalize());
		
		//Resolvendo Paths
		
		System.out.println("\n================");
		System.out.println("Resolvendo Paths\n");
		
		//Primeiramente o que resolver um Path? E basicamente juntar dois Paths em um
		
		Path absoluto = Paths.get(System.getProperty("user.home"));
		Path relativo = Paths.get("Desktop");
		Path arquivo = Paths.get("Arquivo.txt");
		
		//Primeiramente, o que e um Path absoluto e relativo?
		
		//Absoluto - E um caminho completo pra algo, consideramos um Path absoluto quando tem mais de 1 pasta no Path
		//Relativo - E um caminho apenas com uma pasta no Path
		
		Path absolutasso = absoluto.resolve(relativo);
		System.out.println(absolutasso);
		
		//Podemos juntar mais de um...
		
		absolutasso = absoluto.resolve(relativo).resolve(arquivo);
		System.out.println(absolutasso);
		
		//Juntando com arquivo
		System.out.println(absoluto.resolve(arquivo));
		
		//Obs.: Sempre devemos resolver Paths absolutos. Olhe o porque...
		
		System.out.println(relativo.resolve(absoluto));
		//Como podemos ver se a gente resolver um relativo, o java percebe que e um relativo e deixa apenas o absoluto,
		//Porque nao faz sentido resolver um relativo com absoluto, porque o absoluto vai se resolver sozinho
		
		System.out.println(arquivo.resolve(absoluto));
		//Mesma coisa com o arquivo
		
		//E se a gente juntar um arquivo com relativo buga tudo
		System.out.println(arquivo.resolve(relativo));
		
		//Resolvendo um arquivo com relativo ate funciona, mas e melhor nao
		System.out.println(relativo.resolve(arquivo));
		
		//Relativizando Paths

		System.out.println("\n================");
		System.out.println("Relativizando Paths\n");
		
		//O que é relativizar um Path? e basicamente perguntar ao java, o que eu tenho que fazer pra um Path chegar ate
		//O outro Path
		
		//Olhe o exemplo
		
		Path path = Paths.get(System.getProperty("user.home"), "Desktop");
		Path path2 = Paths.get("C:", "Program Files");
		
		System.out.println("path: " + path);
		
		//Como eu faco pro path chegar ate o path2?
		System.out.println("O que eu tenho que fazer? " + path.relativize(path2));
		//Ele falou pra eu voltar tres vezes e depois ir pro Progam Files
		
		//Como eu faco pro path2 chegar ate o path?
		System.out.println(path2.relativize(path));
		
		Path path3 = Paths.get("C:", "Program Files");
		Path path4 = Paths.get("C:", "Program Files", "Common Files", "System");
		
		//path3 ate o path4
		System.out.println(path3.relativize(path4));
		//Como podemos ver o java so passa apenas o que a gente precisa pra chegar no outro
		
		//Mas tem um problema, o java nao consegue achar aonde esta um caminho relativo.
		Path relativo1 = Paths.get("C:");
		Path relativo2 = Paths.get("Program Files");

		//Nao podemos relativizar um relativo com relativo
		//System.out.println(relativo1.relativize(relativo2));
		
		//Nem um absoluto com relativo	
		//System.out.println(path3.relativize(relativo2));
		
		//Basicamente, nunca podemos usar o relativize() com relativo, apenas com absoluto
		
		//Como podemos ver, o java consegue perceber que a gente ja esta no path e nao nos retorna nada.
		Path path3Aux = Paths.get("C:", "Program Files");
		System.out.println(path3.relativize(path3Aux));
	}
}