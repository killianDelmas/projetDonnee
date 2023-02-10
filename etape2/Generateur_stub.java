import java.io.*;
import java.io.FileWriter;
import java.lang.reflect.*;

public class Generateur_stub {

    public Generateur_stub() {
    }

    public static void main (String argv[]) {

        String nomClasse = argv[0];
        File file = new File(nomClasse + "_stub.java");

        try {

            boolean value = file.createNewFile();
            FileWriter resultat = new FileWriter(nomClasse + "_stub.java");

            Class<?> classe = Class.forName(nomClasse);
            String contenu = "public class " + nomClasse + "_stub extends SharedObject implements " + nomClasse + "_itf ";

            Class<?>[] interfaces = classe.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                contenu += ", " + interfaces[i].getName();
            }
            
            
            contenu += " {\n\n";

            contenu += "public " + nomClasse + "_stub(Object o, int id) {\n super(o,id);\n}\n";

            Method[] methodes = classe.getDeclaredMethods();
            for (Method m : methodes) {

                Parameter[] parametres = m.getParameters();
                Class<?> typeRetour = m.getReturnType();
                contenu += "public " + typeRetour.getName() + " " + m.getName() + " (";

                for (int i = 0; i < parametres.length; i++) {
                    contenu += parametres[i].getType().getName() + " " + parametres[i].getName();
                    if (i != parametres.length - 1) {
                        contenu += ", ";
                    }
                }
                contenu += ") {\n";

                if (!typeRetour.getName().equalsIgnoreCase("void")) {
                    contenu += "return ";
                }
                contenu += "((" + nomClasse + ") obj)." + m.getName() + "(";
                for (int i = 0; i < parametres.length; i++) {
                    contenu += parametres[i].getName();
                    if (i != parametres.length - 1) {
                        contenu += ", ";
                    }
                }
                contenu += ");\n";

                contenu += "}\n\n";

            }

            contenu += "}\n";
            resultat.write(contenu);
            resultat.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}