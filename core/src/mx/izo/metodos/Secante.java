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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by isain on 10/11/2016.
 */

public class Secante implements Screen {
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

    //Variables para la funcion
    int grado;
    boolean banderaGrado = false;
    boolean banderaGradoDado = false;
    ArrayList<Integer> coeficientes = new ArrayList<Integer>();
    boolean banderaX0 = false;
    boolean banderaX1=false;
    double x0;
    double x1;
    double x2;
    double tolerancia = 0.01;
    double error = 100;
    double valor = 0;
    int contador=0;
    //Para los resultados de la evaluacion de las x en las funciones
    double fx0 = 0;
    double fx1 = 0;
    boolean datosCompletados=false;



    /*asssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss*/
    public Secante(Plataforma plataforma) {
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
            secante();
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


    public void secante() {
        Gdx.app.log("Entre","a huevo");
        //Bandera que apaga el render
        banderaDatos=false;

        if(!banderaGradoDado) {
            Input.TextInputListener listener = new Input.TextInputListener() {
                @Override
                public void input(String text) {
                    grado = Integer.parseInt(text);
                    banderaGrado = true;
                    banderaGradoDado = true;
                }

                @Override
                public void canceled() {

                }
            };
            Gdx.input.getTextInput(listener, "Inserta el grado de la ecuacion: ", "", "");
            Gdx.app.log("El grado de la ecuacion es ", String.valueOf(grado));
        }
        
        if(banderaGrado){
            //Rellenar los coeficientes
            for (int i = 0; i < grado + 1; i++) {
                Input.TextInputListener listener2 = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        coeficientes.add(Integer.parseInt(text));
                        Gdx.app.log("El grado de la ecuacion es ", String.valueOf(grado));
                        Gdx.app.log("Coeficientes del polinomio ", coeficientes.toString());
                        contador++;
                        if(contador==grado){
                            banderaX0 = true;
                        }
                    }

                    @Override
                    public void canceled() {

                    }
                };
                Gdx.input.getTextInput(listener2, "Inserta el coeficiente","", "");
            }
            //Apagar bandera para que ya no pida mas coeficientes
            banderaGrado=false;
        }

        if(banderaX0){
            Input.TextInputListener listener = new Input.TextInputListener() {
                @Override
                public void input(String text) {
                    x0 = Double.parseDouble(text);
                    banderaX1 = true;
                    banderaX0 = false;
                }

                @Override
                public void canceled() {

                }
            };
            Gdx.input.getTextInput(listener, "Inserta la x0 ", "", "");
        }
        if(banderaX1){
            Input.TextInputListener listener = new Input.TextInputListener() {
                @Override
                public void input(String text) {
                    x1 = Double.parseDouble(text);
                    banderaX1 = false;
                    datosCompletados = true;
                }

                @Override
                public void canceled() {

                }
            };
            Gdx.input.getTextInput(listener, "Inserta la x1 ", "", "");
        }



        //Ciclo para calcular las x
        if(datosCompletados) {
            while (error > tolerancia) {
                //Hacer un ciclo para la evaluacion de x1 y guardarlo en el arrayList de resultados
                for (int i = 0; i < grado + 1; i++) {
                    valor = (coeficientes.get(i) * (Math.pow(x1, grado - i)));
                    //resultadosx1.add(valor);
                    fx1 += valor;
                }
                //Hacer un ciclo para la evaluacion de x0 y guardarlo en el arrayList de resultados
                for (int i = 0; i < grado + 1; i++) {
                    valor = (coeficientes.get(i) * (Math.pow(x0, grado - i)));
                    //resultadosx0.add(valor);
                    fx0 += valor;
                }
                //Calcular x2
                x2 = x1 - (((x1 - x0) / (fx1 - fx0)) * fx1);

                //Hacer la nueva asignacion de variables
                x0 = x1;
                x1 = x2;

                //Calcular el error
                error = Math.abs(x1 - x0);
                fx0 = 0;
                fx1 = 0;
            }
            Gdx.app.log("La raiz es: " ,String.valueOf(x2));
        }
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

