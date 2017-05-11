package mx.izo.metodos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by isain on 10/11/2016.
 */

public class MultMatrices implements Screen {
    // Referencia al objeto de tipo Game (tiene setScreen para cambiar de pantalla)
    private Plataforma plataforma;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;

    // Objeto para dibujar en la pantalla
    private SpriteBatch batch;

    //Texura para los botones
    Texture texturaBtnInsertar;
    Boton btnInsertarDatos;

    //Bandera para entrar al metodo
    boolean banderaDatos = false;
    boolean procesamientoCompletado = false;

    //Variables para la funcion
    int col_m1;
    int fil_m1;
    int col_m2;
    int fil_m2;

    int [][] m1;
    int [][] m2;
    int [][] resultado;

    int valor = 0;

    boolean banderaColM1 = false;
    boolean banderaFilM1 = false;
    boolean banderaColM2 = false;
    boolean banderaFilM2 = false;

    boolean m1creada = false;
    boolean m2creada = false;


    boolean m1final = false;
    boolean m2final=false;
    ArrayList<Integer> valoresm1 = new ArrayList<Integer>();
    ArrayList<Integer> valoresm2 = new ArrayList<Integer>();


    int x1=0;
    int y1=0;

    int contadorM1=0;
    int contadorM2=0;

    boolean datosCompletados1 = false;
    boolean datosCompletados2 = false;

    boolean pinchesBandera=false;

    //Para el texto
    private Texto texto;

    boolean resultados=false;



    /*asssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss*/
    public MultMatrices(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    /* Se ejecuta al mostrar este Screen como pantalla de la app */
    @Override
    public void show() {
        // Crea la cámara/vista
        camara = new OrthographicCamera(Plataforma.ANCHO_CAMARA, Plataforma.ALTO_CAMARA);
        camara.position.set(Plataforma.ANCHO_CAMARA / 2, Plataforma.ALTO_CAMARA / 2, 0);
        camara.update();
        vista = new StretchViewport(Plataforma.ANCHO_CAMARA, Plataforma.ALTO_CAMARA, camara);

        batch = new SpriteBatch();

        cargarRecursos();
        crearObjetos();

        Gdx.input.setInputProcessor(new ProcesadorEntrada());

        // Tecla BACK (Android)
        Gdx.input.setCatchBackKey(true);

        texto = new Texto();
    }

    // Carga los recursos a través del administrador de assets
    private void cargarRecursos() {

        // Cargar las texturas/mapas
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager

        // Texturas de los botones
        assetManager.load("BtmOk.png", Texture.class);    // Cargar imagen

        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
    }

    private void crearObjetos() {
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager
        texturaBtnInsertar=assetManager.get("BtmOk.png");
        btnInsertarDatos = new Boton(texturaBtnInsertar);
        btnInsertarDatos.setPosicion(Plataforma.ANCHO_CAMARA/2,Plataforma.ALTO_CAMARA/2);
        //new int [col_m1][fil_m1]

        //Ingresando dimensiones de las matrices
        Input.TextInputListener listener3 = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                col_m2 = Integer.parseInt(text);
                banderaColM2 = true;
            }
            @Override
            public void canceled() {

            }
        };
        Gdx.input.getTextInput(listener3, "Columnas M2 ", "", "");

        Input.TextInputListener listener4 = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                fil_m2 = Integer.parseInt(text);
                banderaFilM2 = true;
            }
            @Override
            public void canceled() {

            }
        };
        Gdx.input.getTextInput(listener4, "Filas M2 ", "", "");

        Input.TextInputListener listener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                col_m1 = Integer.parseInt(text);
                banderaColM1 = true;
            }
            @Override
            public void canceled() {

            }
        };
        Gdx.input.getTextInput(listener, "Columnas M1 ", "", "");

        Input.TextInputListener listener2 = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                fil_m1 = Integer.parseInt(text);
                banderaFilM1 = true;
            }
            @Override
            public void canceled() {

            }
        };
        Gdx.input.getTextInput(listener2, "Filas M1 ", "", "");


    }

    /*
    Dibuja TODOS los elementos del juego en la pantalla.
    Este método se está ejecutando muchas veces por segundo.
     */
    @Override
    public void render(float delta) { // delta es el tiempo entre frames (Gdx.graphics.getDeltaTime())

        // Dibujar
        borrarPantalla();

        batch.setProjectionMatrix(camara.combined);


        // Entre begin-end dibujamos nuestros objetos en pantalla
        batch.begin();
        btnInsertarDatos.render(batch);
        if(banderaDatos){
            multiplicacion();
        }
        if(resultados){
            int des = 0;
            for (int x=0; x < resultado.length; x++) {
                for (int y = 0; y < resultado[x].length; y++) {
                    texto.mostrarMensaje(batch,String.valueOf(resultado[x][y]),((Plataforma.ANCHO_CAMARA/4)+(des*40)) ,(Plataforma.ALTO_CAMARA/2)-(65*x));
                    des=des+5;
                }
                des=0;
            }
        }
        //Imprimir matriz 1

        batch.end();

    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0, 0, 1, 1);    // r, g, b, alpha
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    // Libera los assets
    @Override
    public void dispose() {
        // Los assets se liberan a través del assetManager
        AssetManager assetManager = plataforma.getAssetManager();
        //assetManager.unload("PantallaDeInicio.png");
        //assetManager.unload("LogoTec.JPG");
        //efecto.dispose();
        //explosion.dispose();
    }


    public void multiplicacion() {
        if(banderaFilM1&&banderaColM1){
            m1= new int [col_m1][fil_m1];
            m1creada = true;
            banderaFilM1=false;
            banderaColM1=false;
        }

        if(m1creada) {
            final int dimension = fil_m1*col_m1;
            for( int x=0;x<dimension;x++){
                Input.TextInputListener listener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        valor = Integer.parseInt(text);
                        valoresm1.add(valor);
                        if(contadorM1==dimension-1){
                            Gdx.app.log("ya me prendi","xD");
                            m1final=true;
                        }
                        contadorM1++;
                    }
                    @Override
                    public void canceled() {
                    }
                };
                Gdx.input.getTextInput(listener,String.valueOf(x), "", "");
            }
            m1creada=false;
        }

        if(m1final) {
            Gdx.app.log("Matriz 1","verga");
            int contador=0;
            //Rutina para rellenar la matriz m1
            for (int x = 0; x < fil_m1; x++) {
                for (int y = 0; y < col_m1; y++) {
                    m1[x][y]=valoresm1.get(contador);
                    contador++;
                }
            }
            //Imprimir matriz
            for (int x=0; x < m1.length; x++) {
                for (int y = 0; y < m1[x].length; y++) {
                    Gdx.app.log(String.valueOf(m1[x][y]), " ");
                }
            }
            m1final=false;
            datosCompletados2 = true;
            pinchesBandera=true;
        }

        if(banderaFilM2&&banderaColM2&&pinchesBandera){
            m2= new int [col_m2][fil_m2];
            m2creada = true;
            banderaFilM2=false;
            banderaColM2=false;
        }

        if(m2creada) {
            final int dimension = fil_m2*col_m2;
            for( int x=0;x<dimension;x++){
                Input.TextInputListener listener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        valor = Integer.parseInt(text);
                        valoresm2.add(valor);
                        if(contadorM2==dimension-1){
                            Gdx.app.log("ya me prendi 2","xD");
                            m2final=true;
                        }
                        contadorM2++;
                    }
                    @Override
                    public void canceled() {
                    }
                };
                Gdx.input.getTextInput(listener,String.valueOf(x), "", "");
            }
            m2creada=false;
        }
        if(m2final) {
            Gdx.app.log("Matriz 2","verga");
            int contador=0;
            //Rutina para rellenar la matriz m1
            for (int x = 0; x < fil_m2; x++) {
                for (int y = 0; y < col_m2; y++) {
                    m2[x][y]=valoresm2.get(contador);
                    contador++;
                }

            }
            //Imprimir matriz
            for (int x=0; x < m2.length; x++) {
                for (int y = 0; y < m2[x].length; y++) {
                    Gdx.app.log(String.valueOf(m2[x][y]), " ");
                }
            }
            m2final=false;
            datosCompletados1=true;
        }
        if(datosCompletados1&&datosCompletados2){
            resultado=producto(m1,m2);
            //Imprimir matriz
            resultados=true;
            for (int x=0; x < resultado.length; x++) {
                for (int y = 0; y < resultado[x].length; y++) {
                    Gdx.app.log(String.valueOf(resultado[x][y]), " ");
                }
            }
            datosCompletados1=false;
            datosCompletados2=false;
        }


    }
    private static int[][] producto(int A[][], int B[][]){
        int suma = 0;
        Gdx.app.log("Haciendo multiplicacion","holiz");
        int result[][] = new int[A.length][B.length];
        for(int i = 0; i < A.length; i++){
            for(int j = 0; j < B.length; j++){
                suma = 0;
                for(int k = 0; k < B.length; k++){
                    suma += A[i][k] * B[k][j];
                }
                result[i][j] = suma;
            }
        }
        return result;
    }

    /*
    Clase utilizada para manejar los eventos de touch en la pantalla
     */
    public class ProcesadorEntrada extends InputAdapter
    {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla
        private String message;

        /*
        Se ejecuta cuando el usuario PONE un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean keyDown(int keycode) {
            return true; // Para que el sistema operativo no la procese
        }
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {

            return true;    // Indica que ya procesó el evento
        }

        /*
        Se ejecuta cuando el usuario QUITA el dedo de la pantalla.
         */
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            transformarCoordenadas(screenX, screenY);
            if(btnInsertarDatos.contiene(x,y)){
                if(resultados){
                    plataforma.setScreen(new Menu(plataforma));
                }
                banderaDatos = true;
            }
            return true;    // Indica que ya procesó el evento
        }
        // Se ejecuta cuando el usuario MUEVE el dedo sobre la pantalla
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return true;
        }
        private void transformarCoordenadas(int screenX, int screenY) {
            // Transformar las coordenadas de la pantalla física a la cámara HUD
            coordenadas.set(screenX, screenY, 0);
            camara.unproject(coordenadas);
            // Obtiene las coordenadas relativas a la pantalla virtual
            x = coordenadas.x;
            y = coordenadas.y;
        }

    }
}

