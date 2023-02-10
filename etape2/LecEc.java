import java.util.Scanner;

public class LecEc implements java.io.Serializable{
    
    public String data;
    public Scanner sc;

    public LecEc(){
        data = "";
    }

    public void ecriture(){
        Scanner sc = new Scanner(System.in);
        System.out.println("En cours d'écriture");
        String str = sc.nextLine();
        System.out.println("Fin ecriture, vous avez ecrit : " + str);
        data = str;
    }

    public void lecture(){
        Scanner sc2 = new Scanner(System.in);
        System.out.println("En cours de lecture (appuyez sur Entrée pour terminer votre lecture)");
        String str = sc2.nextLine();
        System.out.println("Fin lecture, j'ai lu : " + data);

    }

}
