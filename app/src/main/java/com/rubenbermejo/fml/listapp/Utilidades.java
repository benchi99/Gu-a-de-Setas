package com.rubenbermejo.fml.listapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Utilidades {

    Context context;
    ArrayList<ObjetoSetas> datos;

    public Utilidades (Context context) {
        this.context = context;
        inicializarDatos();
    }

    //VERSIÓN
    final public static int VERSION = 6;

    //Tablas
    final public static String NOMBRE_TABLA = "SETAS";

    //Atributos
    final public static String NOMBRE_COLUMNA = "nombre";
    final public static String DESCRIPCION_COLUMNA = "descripcion";
    final public static String NOMBRECOMUN_COLUMNA = "nombreComun";
    final public static String URLLINEA_COLUMNA = "URLlinea";
    final public static String COMESTIBLE_COLUMNA = "comestible";
    final public static String ID_COLUMNA = "id";
    final public static String FAV_COLUMNA = "favorito";
    final public static String IMG_COLUMNA = "imagen";

    public void rellenaBaseDeDatos(SQLiteDatabase bd) {
        System.out.println(datos.size());
        ContentValues cvs = null;
        for (int i = 0; i < datos.size(); i++) {
            cvs = new ContentValues();
            cvs.put(NOMBRE_COLUMNA, datos.get(i).getNombre());
            cvs.put(DESCRIPCION_COLUMNA, datos.get(i).getDescripcion());
            cvs.put(NOMBRECOMUN_COLUMNA, datos.get(i).getnombreComun());
            cvs.put(URLLINEA_COLUMNA, datos.get(i).getURLlinea());
            cvs.put(COMESTIBLE_COLUMNA, datos.get(i).getComestible());
            cvs.put(FAV_COLUMNA, datos.get(i).getFavorito());
            cvs.put(IMG_COLUMNA, datos.get(i).getImagen());

            bd.insert(NOMBRE_TABLA, ID_COLUMNA, cvs);
        }
    }

    public static ArrayList<ObjetoSetas> obtenerListaMasReciente(SetasSQLiteHelper con, String param) {

        SQLiteDatabase db = con.getReadableDatabase();
        Cursor c = null;

        String[] params = { "1" };
        String[] cols = { "*" };

        if (param.equals("normal")) {
            c = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA, null);
        } else if (param.equals("favorito")){
            c = db.query(Utilidades.NOMBRE_TABLA, cols, Utilidades.FAV_COLUMNA + " = ?", params, null, null, null);
        }

        ArrayList<ObjetoSetas> listActual = new ArrayList<>();
        ObjetoSetas seta;

        while (c.moveToNext()) {
            seta = new ObjetoSetas(c.getString(1), c.getString(2), c.getString(3), c.getString(4), intToBool(Integer.parseInt(c.getString(5))), c.getBlob(7));
            seta.setId(c.getInt(0));
            listActual.add(seta);
        }

        return listActual;
    }

    public static byte[] convertirImagenABytes(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(175000);
        bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] blob = baos.toByteArray();
        return blob;
    }

    public static Bitmap convertirBytesAImagen(byte[] blob) {
        Bitmap bmp;
        ByteArrayInputStream bais = new ByteArrayInputStream(blob);
        bmp = BitmapFactory.decodeStream(bais);
        return bmp;
    }

    private Bitmap intABitmap(int img) {
        return BitmapFactory.decodeResource(this.context.getResources(), img);
    }

    private static boolean intToBool(int val) {

        if (val == 0) {
            return false;
        } else {
            return true;
        }

    }

    public static void delElement(SetasSQLiteHelper con, int id){
        SQLiteDatabase db = con.getWritableDatabase();

        String[] param = { String.valueOf(id) };

        try {
            db.delete(NOMBRE_TABLA, ID_COLUMNA + " = ?", param);
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private void inicializarDatos() {
        datos = new ArrayList<>();

        datos.add(new ObjetoSetas("Agaricus Campestris", "Sombrero: Como casi todos los Agaricus, evolucionan desde la forma globosa de su juventud a la convexo extendida en su madurez. Su tamaño varía entre los 3 y los 12 cm de diámetro. Tiene una cutícula gruesa, separable, de color blanco, en la que aparecen escamas más o menos apreciables según el ejemplar, de un color gris cremoso. El margen es muy excedente, que se extiende más alla de la carne del sombrero. Al corte, es estrecho, fino y evoluciona a incurvado.\n" +
                "Laminas: Libres, apretadas de un color blanco rosáceo que cambia a marrón oscuro y más tarde a negro por causa de la aparición de las esporas.\n" +
                "Pie: Cilíndrico, espeso, tenaz, ligeramente más delgado en la base -fusiforme-. Es de color blanco. Mide hasta 7 cm. de longitud y de 1 a 2 cm. diámetro. Tiene un anillo simple, membranoso, fino, estrecho, al principio persistente, más tarde fugaz, pero dejando siempre restos. Se situa en la parte superior del pie. \n" +
                "Carne: Consistente, blanca que al corte adopta un suave color rosáceo, en especial en la unión del pie y sombrero. Sabor y olor muy agradables.\n", "Champiñón Silvestre", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/agaricus-campestris/", true , convertirImagenABytes(intABitmap(R.drawable.agaricus_campestris))));
        datos.add(new ObjetoSetas("Agaricus xanthodermus", "Sombrero: Originalmente es globoso, ovoide, pero siempre recordando una forma trapezoidal, posteriormente evoluciona a convexo extendido. Cuando se daña aparece rápidamente un color amarillo cromo muy característico de la especie. Su margen es, al corte, convoluto a incurvado evolucionando con la edad a decurvado. Perimetralmente, este margen es entero y suele tener restos del velo universal.\n" +
                "Laminas: Libres, apretadas al principio de un color blancuzco con tonos rosados, luego de un rosa muy caracteristico que se mantiene durante casi toda su vida, al final, se oscurece hasta el marrón grisáceo oscuro. Sus arístas son ligeramente más claras.\n" +
                "Pie: Muy esbelto, separable, cilíndrico, hueco, frecuentemente curvado hacia la base. Sus medidas aproximadas son 5-15 cm. de altura por 1 a 1,5 cm. de diámetro. Es de color blanco que pasa a ser amarillo cromo, muy característico, al presionarse o rozarse, especialmente en la base del pie. Tiene un anillo amplio con borde doble y caras diferenciadas, la superior membranosa y consistente y la inferior fugaz y dentada. Esta última frecuentemente se adhiere al pie.\n" +
                "Carne: Compacta y consistente en la juventud. De color blanco que rápidamente cambia a amarillo cromo, especialmente en la base del pie. Al rato cambia a pardo y luego a gris. De sabor desagradable y olor a fenol que, además, se acentúa cuando se cocina.\n","Champiñón amarilleante", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/agaricus-xanthodermus/",  false, convertirImagenABytes(intABitmap(R.drawable.agaricus_xanthoderma))));
        datos.add(new ObjetoSetas("Amanita caesarea", "SOMBRERO: Son grandes, de un diámetro que varía entre los 8 y 25 cm. en ejemplares desarrollados. Es de un color típicamente anaranjado con, en algunos casos, restos blancos del velo universal. Es carnoso, compacto y consistente, aunque se vuelve algo esponjoso con la edad. Evoluciona de globoso a convexo y, finalmente, casi plano. La cutícula es separable, dejando ver una carne con color amarillento. Es de superficie lisa, algo viscosa con la humedad, y brillante sobre todo, de joven. El margen agudo y estriado, más apreciable en sombreros desplegados.\n" +
                "LÁMINAS: Al principio de color amarillo pálido, después amarillo dorado, anchas, libres, numerosas, con la arista flocosa. Tiene abundantes laminillas intercaladas, lamélulas.\n" +
                "PIE: Cilíndrico, más delgado hacia el ápice, amarillo claro, recto y robusto. Es lleno, pero a medida que crece se va deshaciendo desde su centro dándole a esta zona un aspecto distinto, casi hueco. Sus medidas varían desde unos 8 a 20 cm. de altura por de 1 a 3 cm. de diámetro. Es carnoso y de adulto algo esponjoso. Tiene un anillo frágil, del mismo color que el pie, amplio, colgante, abundante, membranoso y estriado en la parte superior. Si lo cortamos, su color varía del amarillo externo al blanco central. Su base es claviforme con volva amplia, blanca o ligeramente grisacea, membranosa, con típica forma de saco. En ejemplares jóvenes, esta volva, velo universal, contiene a la seta en su conjunto con una forma de huevo, desarrollándose más adelante con pie, y sombrero.\n" +
                "CARNE: Blanca, amarillenta bajo la cutícula, espesa, tierna, de sabor agradable y olor suave que pasa a ser desagradable en ejemplares muy adultos.\n","Oronja", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/amanita-caesarea/", true, convertirImagenABytes(intABitmap(R.drawable.amanita_caesarea))));
        //FALTA IMAGEN
        datos.add(new ObjetoSetas("Amanita muscaria", "SOMBRERO: que en abierto puede superar los 15 cm, en un principio globoso pero abriéndose hasta quedar extendido, con el margen acanalado al llegar a la madurez, algo viscoso en tiempo lluvioso. Su color fundamental es el rojo, aunque a veces se difumina hacia tonos anaranjados, y está recubierto de copos blancos, a veces amarillentos y lábiles (pueden perderse con la lluvia). Cutícula separable.\n" +
                "LÁMINAS: blancas, libres y bastante anchas.\n" +
                "PIE: blanco, central y cilíndrico, engrosado en la base en un bulbo que se encuentra adornado por ribetes concéntricos de consistencia algodonosa, a veces tintados de amarillo. Anillo blanco moteado de copos, colgante y persistente.\n" +
                "CARNE: blanca, solo bajo la cutícula adquiere tintes naranjas superficialmente, con sabor suave y olor ligeramente rafanoide.\n", "Falsa Oronja", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/amanita-muscaria/", false, convertirImagenABytes(intABitmap(R.drawable.dano64px))));
        datos.add(new ObjetoSetas("Amanita phalloides", "SOMBRERO: de 6 a 9 cms de diámetro, primero semi-hesférico luego convexo para acabar aplanado, de color verde oliva, con tonos claros, algo amarillentos, presenta fibrillas mas intensas en los bordes, de tonos marrones algo ocres, borde liso con fibrillas, el borde se cuartea en ejemplares adultos, a veces presenta restos de volva.\n" +
                "LÁMINAS: de libres a distantes sobre todo en la madurez, apretadas, blancas con laminillas en los margenes del himenio.\n" +
                "PIE: de 7-9 x 1-2, separable, cilíndrico, un poco más ensanchado en la base de color blanquecino a verdoso, más claro que el sombrero, con fibrillas de tonos verdosos, lleno, luego algo hueco, anillo colgante, persistente,membranoso, con estrías, presenta volva en saco, de color blanca segun avanza por el pie toma color del anillo.\n" +
                "ESPORADA: blanca.\n" +
                "CARNE: blanca, olor a leche agría.\n", "Oronja Mortal", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/amanita-phalloides/", false, convertirImagenABytes(intABitmap(R.drawable.amanita_phalloides))));
        //FALTA IMAGEN
        datos.add(new ObjetoSetas("Amanita ponderosa", "Sombrero: que puede superar los 10 cm, en un principio hemisférico, después convexo, y por último aplanado, bastante carnoso. Su cutícula es lisa y seca, y su color varía según su estado de desarrollo, al emerger de la volva es de color blanquecino, después va pasando a tonos ocre-rosados, para definitivamente volverse pardo rojiza. El margen no es acanalado.\n" +
                "Láminas: en un principio blancas, pero es una de esas pocas Amanitas en las que el color de las láminas no es permanentemente blanco, sino que tienden a volverse cremas. Son algo apretadas y libres con respecto al pie.\n" +
                "Pie: grueso, macizo y lleno, en definitiva, consistente. Suele ser bulboso hacia la base y su color varía entre el blanco y tonos más rosados, pero siempre más claro que el sombrero. Posee un anillo blanco pero muy fugaz y una volva amplia, persistente y membranosa, de color blanco aunque no lo parezca, pues está siempre manchada de tierra.\n" +
                "Carne: blanca, ligeramente rosada al contacto con el aire, gruesa y consistente, de sabor suave y olor a tierra.\n", "Gurumelo", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/amanita-ponderosa/",  true, convertirImagenABytes(intABitmap(R.drawable.dano64px))));
        datos.add(new ObjetoSetas("Amanita rubescens", "SOMBRERO de 4 a 10 cms de diámetro, primero hemisférico, luego convexo para terminar siendo plano, plano-convexo a estar ligeramente conxexo en la madurez, de tonos rojizos, bermellones a ocres, marrón claro, presenta escamas de tonos mas claros casi blanquecinas a ocres claros, concentricas persistentes que desaparecen en la madurez, borde liso, de joven un poco apendiculado.\n" +
                "LÁMINAS libres, con lamélulas, de maduras presentan tonos ferruginosos.\n" +
                "PIE 6-10 x 1,5-2,5, subcilíndico,lleno,algo hueco en su madurez y hacia el sombrero, mas\n" +
                "grueso hacia su base terminando a ser un poco adelgazado, enrojece en la base, de color blanco-rojizo, a ocre-rojizo, presenta un anillo colgante amplio, bastante estriado del mismo color que el pie pero de tonos mas claros, el pie es algo escamoso sobre todo del anillo hacia la base de tonos rojizos claros, presenta una volva circuncisa de tonos rojizos.\n" +
                "ESPORADA blanca.\n" +
                "CARNE: dura, olor débil, enrojece en las zonas dañadas.\n" +
                "\n", "Oronja vinosa", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/amanita-rubescens/", true, convertirImagenABytes(intABitmap(R.drawable.amanita_rubescens))));
        datos.add(new ObjetoSetas("Amanita Verna", "Descripción: Sombrero de 4 a 8 cm de diámetro, al principio hemisférico o globoso, después convexo, pero pronto se extiende y aparece plano-convexo; margen regular, incurvado, liso sin estrías. Cutícula lisa, fina, untosa o algo viscosa en tiempo húmedo, seca, satinada y de color blanco puro de joven, al madurar puede tomar un color blanco-crema sobretodo en lo alto del disco.\n" +
                "Láminas: ventrudas, muy apretadas, blancas y delicadas.\n" +
                "Pie: alto, firme, cilíndrico, un poco atenuado en lo alto, de superficie sedosa bajo el anillo, encima es algo estriado. Anillo alto, membranoso, frágil, blanco, a veces con la parte superior estriada. Base algo más gruesa, enfundada en una volva blanca con forma de saco, membranosa y frágil que normalmente aparece muy enterrada y puede deshacerse o desaparecer al desenterrar la seta.\n" +
                "Carne: delgada, frágil, de color blanco, con sabor y olor poco pronunciados.\n" +
                "\n", "Cicuta blanca", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/amanita-verna/",  false, convertirImagenABytes(intABitmap(R.drawable.amanita_verna))));
        datos.add(new ObjetoSetas("Armillaria mellea", "SOMBRERO: De 4 a 13 cm ,cónico-convexo al principio para terminar aplanado y ligeramente deprimido. Margen liso de joven ,luego estriado por transparencia,de enrollado a ligeramente decurvado en la madurez.Cutícula mate, seca,embebida con la humedad,de color amarillo miel,con la madurez va tomando una tonalidad marronácea ocre, cubierta de escamas amarillentas fugaces con la edad,, centro marrón oscuro con el borde casi blancuzco por los restos del velo que van desaparieciendo en el crecimiento.\n" +
                "LÁMINAS: subdecurrentes con un número de entre 50 y 60, laminillas entre 1 y 5.Alt Lxalt C=1x 0,2 cm, de color blanco a ocre-marrón manchándose de pardo rojizo con la edad. Aristas algo onduladas u de un color marrón más oscuro que sus caras.\n" +
                "PIE: de 4 a 14 cmx0,8-2 cm cilíndrico,curvado,de lleno a hueco con la edad,un poco atenuado y engrosado hacia la base adornado con un anillo membranoso y persistente de color blancuzco, debajo del cual se encuentra una zona anular amarillenta, fibriloso, de un color marronáceo llegando a casi negro con flocones pálidos en toda su longitud.\n" +
                "ESPORADA: blanca\n" +
                "CARNE: delgada, escasa en algunas zonas, blanca con reflejos encarnados. Olor como a moho y sabor de dulce a amargo, que irrita a la garganta después de una masticación prolongada.\n", "Armilaria de color miel.", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/armillaria-mellea/",  true, convertirImagenABytes(intABitmap(R.drawable.armillaria_mellea))));
        datos.add(new ObjetoSetas("Boletus aereus", "SOMBRERO: de hasta 20 cm, carnoso, de joven hemisférico, después convexo, con una cutícula lisa y glabra de color pardo oscuro, en ocasiones casi negro, y de tacto aterciopelado que pierde con el tiempo.\n" +
                "TUBOS: largos, de color netamente blanco, que después pasan a amarillento y acaban siendo verdosos, libres o adnatos sobre el pie.\n" +
                "POROS: concoloros, bastante prietos  y redondos.\n" +
                "PIE: central, muy robusto, macizo, en su primera etapa se muestra engrosado en la base, incluso ventrudo,  adoptando posteriormente una forma cilíndrica. Su color es ocre, mucho más pálido que el sombrero y posee una retícula muy fina que puede llegar a abarcar los dos tercios superiores del pie.\n" +
                "CARNE: gruesa, compacta, de color blanco, que además posee olor y sabor muy agradables.\n", "Hongo Negro", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/boletus-aereus/", true, convertirImagenABytes(intABitmap(R.drawable.boletus_aereus))));
        datos.add(new ObjetoSetas("Boletus aestivalis", "Sombrero de entre 10 y 15 cm de diámetro, que pasa de ser hemisférico a convexo, para después quedarse plano-convexo. Su color más habitual es el marrón, aunque el tono varía en función de las condiciones del espécimen, pudiendo adoptar tonos grisáceos en época de sequía. Su cutícula es lisa y finamente pubescente, sobre todo de joven, siendo además separable de la carne del sombrero con relativa facilidad.\n" +
                "Tubos prácticamente libres con respecto al pie, largos y finos, de color blanco en los ejemplares jóvenes, luego amarillo oliva, inmutables.\n" +
                "Poros redondos de color blanco, que evolucionan hacia el amarillo, y al final son verdosos. Inmutables al tacto, si acaso se ensucian un poco en las heridas.\n" +
                "Pie grueso y corto en su primera etapa, después cilíndrico y alargado, de color más claro que el sombrero, en ocasiones incluso blanco. Suele tener una malla visible sobre todo en su parte alta.\n" +
                "Carne gruesa y consistente de joven, con el tiempo se vuelve más blanda, sobre todo en el sombrero. De color blanco en todas las zonas, inmutable al corte. Olor y sabor agradables.\n", "Boleto reticulado", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/boletus-aestivalis/", true, convertirImagenABytes(intABitmap(R.drawable.boletus_aestivalis))));
        datos.add(new ObjetoSetas("Boletus edulis", "SOMBRERO: que en ocasiones alcanza grandes dimensiones, hasta 30 cm, de color muy variable, predominando el pardo, con el borde más claro. Muy carnoso, se muestra ligeramente viscoso en tiempo de lluvias, después seco. Su forma va desde hemisférica a convexa, aplanándose en la vejez.\n" +
                "TUBOS: blancos cuando es joven, se vuelven amarillos con el tiempo y finalmente verdosos, son separables con facilidad del sombrero y no llegan al pie (libres). Poros concolores, se muestran inmutables tanto al tacto como al corte.\n" +
                "PIE: lleno y duro, ventrudo cuando joven, después adopta una forma más cilíndrica, tan carnoso o más que el propio sombrero, de color generalmente blanco y a veces, no siempre, con una fina retícula en la parte superior.\n" +
                "CARNE: blanca e inmutable al contacto con el aire, dura cuando el ejemplar es joven, se va volviendo más esponjosa con la edad, de olor fúngico agradable y sabor a avellana.\n", "Tontullo", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/boletus-edulis/", true, convertirImagenABytes(intABitmap(R.drawable.boletus_edulis))));
        datos.add(new ObjetoSetas("Boletus erythropus", "Sombrero: En un principio es semiesférico que evoluciona a convexo, a veces con formas irregulares, onduladas. Es bastante grande, su diámetro que puede superar los 20 cm. La cutícula, no separable de la carne, es ligeramente afieltrada de joven, mate y seca cuando crece y algo viscosa en tiempo húmedo. De un color marrón variable, pardo rojizo, ocre grisáceo, más claro en el borde cuando es mayor. Se mancha de rojo oscuro en las zonas dañadas por mordeduras y azul oscuro en las rozadas. Su margen es excedente.\n" +
                "Himenio: Los tubos de un color amarillo anaranjado que viran inmediatamente al azul al dañarlos. Son libres y largos, aproximadamente de 10 a 20 mm. y fácilmente separables de la carne. Sus poros son circulares, pequeños y de color naranja a rojo intenso que también se vuelven azul oscuro si se rozan.\n" +
                "Pie: Consistente, duro, robusto, frecuentemente más engrosado en su base y de color ocre-anaranjado. De 5-17 cm. de largo por 2-6 cm. de diámetro está finamente decorado con un punteado con pequeñas granulaciones de color rojo intenso, no tiene retícula como otros boletus. Todo el pie adopta color azul intenso al rozarse.\n" +
                "Carne: De color amarillo pálido que cambia rápida y progresivamente a azul intenso al cortarse, al cabo del tiempo se vuelve violeta-verdoso. Es dura, consistente y espesa. Olor y sabor muy agradables.\n", "Pie Rojo", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/boletus-erythropus/", true, convertirImagenABytes(intABitmap(R.drawable.boletus_erythropus))));
        datos.add(new ObjetoSetas("Boletus pinicola", "SOBRERO: Cutícula de color marrón rojizo -caoba- uniforme, lisa, y de difícil separación de la carne. Como es habitual en los boletus es de forma hemiesférica que evoluciona a plano convexa, llegando a medir hasta 30 cm. de diámetro. Margen decurvado que se convierte en plano y excedente en la madurez.\n" +
                "HIMENIO: Esta compuesto por tubos casi libres, separables de la carne. Primeramente son de color blanco, luego amarillentos, pasando rápidamente a amarillo verde oliva. Poros redondos del mismo color que los tubos.\n" +
                "PIE: Muy grueso, sólido y muy ventrudo hacía la base, tanto que en muchos casos supera en diámetro al sombrero. De color parecido al del sombrero pero más claro, decorado con una reticula de mallas poligonales de tonos cremas a rojos en el ápice, y más diluida en el resto.\n" +
                "CARNE: Consistente, espesa, blanca, debajo de la cutícula de tonos vinosos. Olor y sabor agradables, dulces y prefumado. Una vez desecado el olor es mucho más intenso.\n", "Pinicola", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/boletus-pinicola/", true, convertirImagenABytes(intABitmap(R.drawable.boletus_pinicola))));
        datos.add(new ObjetoSetas("Calocybe gambosa", "SOMBRERO: De 4 a 10 cm de diámetro, al principio convexo que se va aplanando con la edad. Margen irregular, incurvado en los ejemplares jóvenes para irse extendiendo en la madurez.Cutícula separable hasta la mitad del sombrero, lisa, seca y sedosa, de color blanco, blanco crema a blanco grisáceo.\n" +
                "LÁMINAS: apretadas (Sobre un ejemplar de 4 cm: L=85-99, l=3-5), estrechas (Ancho de la carne subhimenial a mitad del radio/ anchura laminar=0,5/0,25=2)ADNATAS; con arista entera a ligeramente ondulada.\n" +
                "PIE: De 3-10 x 1-2 cm, cilíndrico, curvándose con la edad, de engrosado a atenuado, fibriloso, de color blancuzco más oscuro en la base en ejemplares maduros.\n" +
                "ESPORADA: Blanco crema.\n" +
                "CARNE: Compacta, espesa, blanca con olor y sabor harinosos.\n", "Seta de San Jorge", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/calocybe-gambosa/",true , convertirImagenABytes(intABitmap(R.drawable.calocybe_gambosa))));
        datos.add(new ObjetoSetas("Cantharellus cibarius", "SOMREBRO: que puede superar los 5 cm de diámetro, de color fundamentalmente amarillo, a veces muy pálido y otras casi naranja, lo que da pie a la existencia de unas cuantas variedades. Su forma es plano-convexa de joven, pero se va deprimiendo y hundiendo por el centro con el tiempo. Su cutícula es lisa y el margen suele estar enrollado y lobulado irregularmente.\n" +
                "LÁMINAS: que en realidad no son tales, sino pliegues bifurcados y muy decurrentes, también de un color amarillo muy llamativo, prácticamente concolores con el sombrero.\n" +
                "PIE: lleno y macizo, atenuado hacia la base, más bien corto, de color amarillo, concolor o algo más pálido que el resto del carpóforo.\n" +
                "CARNE: espesa en la zona central del sombrero, más delgada hacia el borde, de color amarillento, olor agradable afrutado y sabor suave.\n", "Rebozuelo", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/cantharellus-cibarius/", true, convertirImagenABytes(intABitmap(R.drawable.cantharellus_cibarius))));
        datos.add(new ObjetoSetas("Clathrus ruber", "Peridio que se presenta inicialmente en forma de un huevo de consistencia gelatinosa y blanda, de color blanco, a veces manchado de ocre. En la parte inferior tiene un hilo que le une al micelio, y en la superior unos surcos que parten hacia la base con distintas direcciones, sin ser paralelos. Cuando el huevo madura estos surcos se abren dando paso al carpóforo.\n" +
                "Carpóforo de forma generalmente ovoide, a veces incluso esférica, sésil, carente por lo tanto de pie. Es como una especie de reja con huecos amplios, o como si fuera un balón hueco por dentro, con una altura que suele sobrepasar los 5 cm. Su color es ciertamente llamativo, rojo vivo. Lo que queda del huevo original se une a la parte inferior cual si de una volva se tratara.\n" +
                "Gleba incrustada en la parte interior de la mencionada reja, de color verde oliváceo, con un olor fétido acusado que atrae a los insectos contribuyendo a su reproducción.\n" +
                "Carne prácticamente nula, su olor en forma de huevo es rafanoide, al igual que su sabor, después de abierto es insoportable, fétido.\n", "Ninguno", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/clathrus-ruber/", false, convertirImagenABytes(intABitmap(R.drawable.clathrus_ruber))));
        datos.add(new ObjetoSetas("Clitocybe dealbata", "Sombrero alcanza los 4 cm. de diámetro, inicialmente hemiesférico y luego aplanado con depresión central.\n" +
                "Color blanco en tiempo seco que se torna crema con la humedad\n" +
                "Láminas blancas, desiguales, decurrentes o arqueadas.\n" +
                "Pie es cilíndrico, recto o curvado de 2 a 4 cm. y de color blanco crema\n" +
                "Esporas son blancas\n" +
                "Carne es blanca con tonos rosados, delgada y firme, de olor farináceo y sabor dulce\n", "Ninguno", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/clitocybe-dealbata/", false, convertirImagenABytes(intABitmap(R.drawable.clitocybe_dealbata))));
        datos.add(new ObjetoSetas("Clitocybe gibba", "SOMBRERO: De 4 a 6 cm, inicialmente convexo, con un pequeño mamelón,abriéndose paulatinamente hasta adquirir una forma embudada característica, margen regular o lobulado. Cutícula separable, lisa, algo flocosa en el margen de color beige rosáceo.\n" +
                "LÁMINAS: Apretadas, muy decurrentes, de color blanco, con reflejos rosáceos hacia el margen con la edad.\n" +
                "PIE: De 3 a 5 cm, cilíndrico, fibrilloso, de color beige claro, ensanchado en la base , que presenta un revestimiento afieltrado de color blanco.\n" +
                "ESPORADA: Blanca‐crema.\n" +
                "CARNE: Elástica, escasa, blanca. Olor afrutado y sabor dulce y agradable.\n", "Inbutu", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/clitocybe-gibba/", true, convertirImagenABytes(intABitmap(R.drawable.clitocybe_gibba))));
        datos.add(new ObjetoSetas("Craterellus cornucopioides", "Sombrero de hasta 10 cm de diámetro, de color variable según el grado de humedad del espécimen, desde negro a gris mate, con una cutícula lisa o ligeramente veteada de fibrillas y con el borde lobulado de manera irregular. Su forma es de trompeta y posee una cavidad en el centro que se prolonga casi hasta la base del pie.\n" +
                "Láminas inexistentes, el himenio de esta especie es completamente liso, y de color gris ceniciento, si acaso débilmente arrugado.\n" +
                "Pie que pudiéramos considerar como una mera prolongación del sombrero, como hemos mencionado es hueco, y su color es similar al del himenio o ligeramente más oscuro.\n" +
                "Carne  escasa, de consistencia elástica, de gris a negruzca, con olor aromático agradable y buen sabor.\n", "Trompeta de los muertos", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/craterellus-cornucopioides/", true, convertirImagenABytes(intABitmap(R.drawable.craterellus_cornucopioides))));
        datos.add(new ObjetoSetas("Craterellus lutescens", "SOMBRERO: De 0,4 a 6 cm, primeramente plano convexo, luego embudado, perforado en el centro, como una trompeta, margen delgado, incurvado y ondulado de forma irregular. Cutícula lisa al principio, luego arrugada, vellosa-escamosa sobre todo hacia el margen, de color pardo grisáceo sobre fondo amarillento-anaranjado. Himenio formado, en vez de láminas, por pliegues sutiles, poco marcados, de color amarillo naranja.\n" +
                "PIE: De 2,5-8 x 0,3-1,2 cm, cilíndrico, hueco, atenuado en la base, algo aplastado, acanalado, de color dorado con reflejos naranjas, más pálido en la zona basal.\n" +
                "ESPORADA: Blanco-crema.\n" +
                "CARNE: Flexible, delgada, blanca amarillenta de olor afrutado bastante agradable y sabor\n" +
                "dulce.\n", "Rebozuelo anaranjado", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/cantharellus-lutescens/", true, convertirImagenABytes(intABitmap(R.drawable.craterellus_lutescens))));
        datos.add(new ObjetoSetas("Entoloma sinuatum", "Sombrero: Son de forma convexa que evolucionan a plano convexa e incluso, en ejemplares muy maduros, deprimidos. Tienen un mamelón ancho y poco apreciable. El aspecto en general es robusto,carnoso, llegando a medir hasta 18 cm. de diámetro. El color de su cutícula es gris claro cremoso, y esta, es difícilmente separable. Tiene pequeñas fibrillas radiales que se aprecian claramente con lupa. El margen es incurvado en ejemplares jóvenes, más tarde es plano y ondulado.\n" +
                "Láminas: Son libres o adnatas, de color amarillento cremoso que pasa a rosa cuando maduran las esporas. Son anchas, de medianamente apretadas a espaciadas y con lamélulas.\n" +
                "Pie: De aspecto robusto, cilíndrico de 6-16 X 1-3 cm., recto o ligeramente sinuoso, a veces bulboso. Duro, generalmente lleno, en algunas ocasiones y en ejemplares maduros es hueco. De color blanquecino con manchas amarillentas que son más abundantes con la edad.\n" +
                "Carne: Blanca, consistente, fibrosa.\n" +
                "De sabor y olor agradables, a harina fresca.\n", "Engañosa", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/entoloma-sinuatum/", false, convertirImagenABytes(intABitmap(R.drawable.entoloma_sinuatum))));
        datos.add(new ObjetoSetas("Hydnum repandum", "Sombrero: Tiene un diámetro que varía entre los 3 y los 15 cm, en ocasiones mayor. Priemeramente es convexo, aplanandose y deformandose con la edad. De colores cremosos, blanquecinos, más tostados en su juventud. Estos tonos se mantienen en el centro del sombrero difuminandose hacia los bordes con el blanco. Como hemos dicho los ejemplares maduros son de forma irregular, lobulada, ondulada, con un margen grueso, enrollado que evoluciona a incurvado. La cutícula es algo aterciopelada, seca y mate.\n" +
                "Himenio: Está formado por aguijones cónicos de color crema claro que oscurecen un poco con la maduración. Son de lonjitud variable (0,3 a 0,6 cm.), numerosos, apretados, y decurrentes por el pie. Se desprenden muy fácilmente de la carne del sombrero.\n" +
                "Pie: Es cilíndrico, corto y macizo, carnoso, y a veces descentrado. De colores parecidos, algo más claros, a los de todo el conjunto del carpóforo: cremas blanquecinos. Forma una unidad con el sombrero. Mide de 1 a 3 cm. de diámetro por 2 a 9 cm. de altura.\n", "Gamuza", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/hydnum-repandum/", true, convertirImagenABytes(intABitmap(R.drawable.hydnum_repandum))));
        datos.add(new ObjetoSetas("Lactarius deliciosus", "SOMBRERO: De 40 a 150 (200) mm; al principio convexo, después plano convexo con el centro algo deprimido; al final suele embudarse. De joven el borde se enrolla para adentro, luego se va extendiendo. La cutícula no es separable, lisa y algo pruinosa; de color anaranjado claro tirando a zanahoria, con unas bandas concéntricas algo más oscuras, que están salpicadas de manchas algo deprimidas y más densas.\n" +
                "LÁMINAS: Al principio adnatas luego decurrentes en la madurez; estrechas, prietas con amélulas,de color anaranjado o anaranjado-rojizo.\n" +
                "PIE: De 30-80 x 10-30 mm; cilíndrico, atenuado en la base; de recto a curvado; de superficie pruinosa, blancuzca sobre fondo anaranjado-zanahoria con motas más oscuras\n" +
                "ESPORADA: blanca\n" +
                "CARNE: Dura, compacta, se parte como la tiza; al corte blanco-amarillento o blancoanaranjado.\n" +
                "Color zanahoria en el exterior del sombrero y en la corteza del pie. Olor y sabor resinoso. Látex anaranjado, al rato se mancha de verde el sombrero, láminas y pie con la manipulación.\n", "Níscalo", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/lactarius-deliciosus/", true, convertirImagenABytes(intABitmap(R.drawable.lactarius_deliciosus))));
        datos.add(new ObjetoSetas("Lactarius quieticolor", "Sombrero de hasta 10 cm de diámetro, convexo y con el borde enrollado cuando joven, después plano e infundibuliforme en la vejez, de color naranja pálido, mate y seco, recubierto de una especie de pruina blanquecina en los ejemplares jóvenes. Débilmente zonado o algo satinado.\n" +
                "Láminas de color naranja pero más apagado que en el lactarius Deliciosus, de adnatas a decurrentes, se manchan de verde en las zonas dañadas o manoseadas, son por lo general apretadas y poseen algunas laminillas intercaladas.\n" +
                "Pie central, cilíndrico, de color similar al del sombrero o incluso de un tono más apagado, que tiende a volverse hueco rápidamente, poco o nada escrobiculado.\n" +
                "Carne firme, de color naranja pálido, casi amarillenta, exuda un látex de color naranja que en la desecación se torna rojo vinoso. Se tiñe de verde en las partes dañadas, su olor es agradable y su sabor en crudo es algo acre.\n", "Níscalo", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/lactarius-quieticolor/", true, convertirImagenABytes(intABitmap(R.drawable.lactarius_duieticolor))));
        datos.add(new ObjetoSetas("Lactarius salmonicolor", "Sombrero de 8 a 15 cm, tiene una superficie de aspecto traslúcido, cuyo color va desde el amarillo-anaranjado al casi rosado (salmón), sin manchas verdes. Es raro notar zonificaciones evidentes y carece de las decoraciones de gotas compenetradas caracterí-sticas de Lactarius deliciosus.\n" +
                "Láminas son adheridas o ligeramente decurrentes por un diente pequeño, bastante apretadas, con la presencia de muchas lamélulas interpuestas y el color, inicialmente anaranjado, puede adquirir tonalidades rosado-salmón, brillantes, más que cualquier otra especie de su grupo. Finalmente son casi de color ocre. Raramente se nota alguna esfumación verduzca.\n" +
                "Pie es cilíndrico, casi regular, bastante macizo, rápidamente se vuelve hueco, del mismo color del sombrero o incluso más intenso. Puede presentar hoyuelos.\n" +
                "Carne blanca en la parte central y naranja vivo en la periferia. Látex naranja, tornándose rojo vinoso al cortar, es de sabor algo amargo.\n", "Níscalo de abeto", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/lactarius-salmonicolor/", true, convertirImagenABytes(intABitmap(R.drawable.lactarius_dalmonicolor))));
        datos.add(new ObjetoSetas("Lactarius sanguifluus", "Sombrero: con un diámetro que puede oscilar en los ejemplares adultos entre los 5 y los 10 cm, ocasionalmente pueden ser algo más grandes. Suele tener una forma convexa durante buena parte de su desarrollo, con el centro umbilicado, después se aplana bastante, pero esta especie no suele llegar a embudarse. Su superficie es seca, afieltrada de joven y posteriormente lisa y glabra, de un color ocráceo pálido con cierto matiz anaranjado, pero nunca netamente naranja, en muchos ejemplares no está zonada en absoluto, siendo vagamente zonada en otros. Margen enrollado en los individuos jóvenes, luego extendido y regular.\n" +
                "Láminas: algo decurrentes, desiguales en su configuración, bastante apretadas y con laminillas, al principio tienen un color pálido, entre ocráceo y anaranjado, adquiriendo pronto tonos rojo vinosos o púrpuras, por las heridas exudan un látex de color rojo vinoso.\n" +
                "Pie: generalmente corto, de aspecto robusto, lleno en los ejemplares jóvenes y después hueco, de color claro, a veces casi blanco pero con matiz purpúreo, es habitual la presencia de escrobículos de color rojo vinoso oscuro.\n" +
                "Carne: espesa y maciza de color ocráceo pálido, a veces casi blanca, teñida al corte por el látex que exuda, de un color rojo vinoso, púrpura al secarse. Su olor es agradable y su sabor suave aunque ligeramente picante en crudo.\n", "Níscalo", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/lactarius-sanguifluus/", true, convertirImagenABytes(intABitmap(R.drawable.lactarius_sanguifluus))));
        datos.add(new ObjetoSetas("Lactarius vinosus", "Sombrero hasta 12, de convexo a extendido, hundido en el centro.\n" +
                "Cutícula zonada y concéntrica, anaranjada-rojiza, a veces con manchas verdosas.\n" +
                "Láminas adherentes y apretadas, color rosa-lila, con manchas verdosas.\n" +
                "Pie corto, cilíndrico, atenuado en la base, escrobiculado y concoloro con el sombrero.\n" +
                "Carne prieta, densa y quebradiza, de color rojizo suave. Látex color vinoso, oscuro, poco abundante.\n", "Níscalo", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/lactarius-vinosus/", true, convertirImagenABytes(intABitmap(R.drawable.lactarius_vinosus))));
        datos.add(new ObjetoSetas("Lepista Nuda", "Sombrero: Evoluciona de una forma convexa e su juventud a plana con un mamelón amplio y suave en la madurez. Su diámetro varía desde los 5 cm. a los 15cm. La cutícula es separable de la carne y tiene un color violeta, azul violeta o marrón violaceo o incluso los dos, difuminandose del centro, marrón, a los bordes, violeta. Es viscosa en tiempo húmedo. Los tonos más violetas se pierden con la edad o la sequedad del ejemplar, pudiendo quedarse en un color crema uniforme. Su margen suele ser concoloro o un poco más violeta, no está estriado, es liso y fino e incurvado en su juventud.\n" +
                "Laminas: De color lila violaceo, apretadas, sinuosas, escotadas o adherentes o un poco decurrentes. Tiene numerosas lamélulas. Con la edad el color lilaceo se vuelve más crema. Las láminas se desprenden facilmente del sombrero.\n" +
                "Pie: Cilíndrico, de base claviforme o suavemente bulbosa. Mide de 5 a 10 cm. de altura por 1 a 2 cm. de diámetro. Es concoloro a todo el conjunto, violaceo. Este color es más apreciable en su base. También se pierde con el paso del tiempo o la sequedad. Su textura es fibrosa, y de consistencia carnosa. En su base es común que aparezca micelio blancoviolaceo con hojarasca y tierra fuertemente adherida.\n" +
                "Carne: De color blanco con reflejos pardos o violetas cerca de la cutícula y de la base del pie. Consistente, tierna, algo esponjosa en tiempo muy humedo.\n" +
                "De olor afrutado muy característico de la especie. Sabor suave, dulce, algo ácido.\n", "Pie azúl", "https://www.cestaysetas.com/informacion-micologica/guia-de-setas/lepista-nuda/", true, convertirImagenABytes(intABitmap(R.drawable.lepista_nuda))));
    }

}
