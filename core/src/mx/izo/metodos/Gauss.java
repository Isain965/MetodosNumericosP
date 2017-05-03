package mx.izo.metodos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by isain on 10/11/2016.
 */

public class Gauss implements Screen {
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

    //Variables globales
    boolean banderaDatos = false;

    int numeroEcuaciones;
    boolean numeroDeEcuaciones=false;
    double[][]A;
    double[] b;


    int contAprox = 0;
    ArrayList<Float> aproximaciones = new ArrayList<Float>();
    boolean banderaAprox = false;
    boolean banderaCoeficientes = false;

    ArrayList<Integer> valoresm1 = new ArrayList<Integer>();
    int contadorM1=0;
    boolean m1final=false;
    boolean banderaPincheProfr = false;
    boolean banderaDatosCompletados = false;
    boolean procesamientoAcabado = false;
    double[] x;



    /*asssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss*/
    public Gauss(Plataforma plataforma) {
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
        //Pedir numero de ecuaciones
        Input.TextInputListener listener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                numeroEcuaciones = Integer.parseInt(text);
                numeroDeEcuaciones = true;
            }
            @Override
            public void canceled() {
            }
        };
        Gdx.input.getTextInput(listener, "Num de Ec. del sistema ", "", "");
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
            eliminacionGauss();
        }

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
    public void eliminacionGauss(){
        if(numeroDeEcuaciones){
            numeroDeEcuaciones=false;
            A = new double[numeroEcuaciones][numeroEcuaciones];
            b=new double[numeroEcuaciones];
            banderaAprox = true;
            banderaDatos = false;
        }
        if(banderaAprox){
            for(int i=0; i<numeroEcuaciones;i++){
                //Pedir numero de ecuaciones
                Input.TextInputListener listener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        float valor = Float.parseFloat(text);
                        aproximaciones.add(valor);
                        banderaAprox = false;
                        contAprox++;
                        if(contAprox==numeroEcuaciones){
                            banderaCoeficientes=true;
                            Gdx.app.log("Llegue "," asda");
                        }
                    }
                    @Override
                    public void canceled() {
                    }
                };
                Gdx.input.getTextInput(listener, "Aproximacion numero: "+i, "", "");
            }
        }

        if(banderaCoeficientes){
            banderaCoeficientes=false;
            final int dimension = numeroEcuaciones*numeroEcuaciones;
            for( int x=0;x<dimension;x++){
                Input.TextInputListener listener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        int valor = Integer.parseInt(text);
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

        }
        if(m1final){
            Gdx.app.log("Matriz 1","verga");
            int contador=0;
            //Rutina para rellenar la matriz m1
            for (int x = 0; x < numeroEcuaciones; x++) {
                for (int y = 0; y < numeroEcuaciones; y++) {
                    A[x][y]=valoresm1.get(contador);
                    contador++;
                }
            }
            //Imprimir matriz
            for (int x=0; x < A.length; x++) {
                for (int y = 0; y < A[x].length; y++) {
                    System.out.print(String.valueOf(A[x][y])+" ");
                }
                System.out.println();
            }
            m1final=false;
            banderaPincheProfr = true;
        }
        if(banderaPincheProfr){
            banderaPincheProfr=false;
            for(int i=0;i<numeroEcuaciones;i++){
                b[i]=aproximaciones.get(i);
                System.out.print(b[i]+" ");
            }
            banderaDatosCompletados = true;
        }
        if(banderaDatosCompletados){
            banderaDatosCompletados=false;
            x = lsolve(A,b);
            procesamientoAcabado=true;
        }
        if(procesamientoAcabado){
            procesamientoAcabado=false;
            System.out.println();
            for (int i = 0; i < numeroEcuaciones; i++) {
                System.out.println(("x"+i+" = ")+(x[i]));
            }
        }
    }
    private final double EPSILON = 1e-10;

    public double[] lsolve(double[][] A, double[] b) {
        int N  = b.length;

        for (int p = 0; p < N; p++) {

            // find pivot row and swap
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double   t    = b[p]; b[p] = b[max]; b[max] = t;

            // singular or nearly singular
            if (Math.abs(A[p][p]) <= EPSILON) {
                throw new RuntimeException("La matriz es linealmente dependiente.");
            }

            // pivot within A and b
            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        // back substitution
        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
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

