package mx.izo.metodos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by isain on 10/11/2016.
 */
public class PrincipalPantalla implements Screen {
    // Referencia al objeto de tipo Game (tiene setScreen para cambiar de pantalla)
    private Plataforma plataforma;

    // La cámara y vista principal
    private OrthographicCamera camara;
    private Viewport vista;

    // Objeto para dibujar en la pantalla
    private SpriteBatch batch;



    // Opciones
    private Texture texturaRegresar;
    private Boton btnRegresar;

    private Texture texturaTec;
    private Boton btnTec;

    private float tiempoJuego=0;

    //Botones de la historia
    private Texture texturaHistoria1;
    private Texture texturaHistoria2;
    private Texture texturaHistoria3;
    private Texture texturaHistoria4;
    private Texture texturaHistoria5;
    private Texture texturaHistoria6;
    private Texture texturaHistoria7;
    private Texture texturaMenu;
    private Boton btnHistoria1;
    private Boton btnHistoria2;
    private Boton btnHistoria3;
    private Boton btnHistoria4;
    private Boton btnHistoria5;
    private Boton btnHistoria6;
    private Boton btnHistoria7;
    private Boton btnMenu;


    //Musica de fondo
    private Music musicFondo;

    private EstadosPantalla estadoPantalla;


    public PrincipalPantalla(Plataforma plataforma) {
        this.plataforma = plataforma;
    }

    /*
    Se ejecuta al mostrar este Screen como pantalla de la app
     */
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

        estadoPantalla = EstadosPantalla.INICIANDO;
    }

    // Carga los recursos a través del administrador de assets
    private void cargarRecursos() {

        // Cargar las texturas/mapas
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager

        assetManager.load("PantallaDeInicio.png", Texture.class);    // Cargar imagen
        // Texturas de los botones

        assetManager.load("LogoTec.JPG",Texture.class);

        //Recuersos para la historia
        assetManager.load("Historia1.jpg",Texture.class);
        assetManager.load("Historia2.jpg",Texture.class);
        assetManager.load("Historia3.jpg",Texture.class);
        assetManager.load("Historia4.jpg",Texture.class);
        assetManager.load("Historia5.jpg",Texture.class);
        assetManager.load("Historia6.jpg",Texture.class);
        assetManager.load("Historia7.jpg",Texture.class);
        assetManager.load("back.png",Texture.class);

        // Se bloquea hasta que cargue todos los recursos
        assetManager.finishLoading();
    }

    private void crearObjetos() {
        AssetManager assetManager = plataforma.getAssetManager();   // Referencia al assetManager
        // Carga el mapa en memoria
        texturaRegresar = assetManager.get("PantallaDeInicio.png");

        texturaTec = assetManager.get("LogoTec.JPG");


        btnRegresar = new Boton(texturaRegresar);

        btnTec = new Boton(texturaTec);

        //Botones de historia
        texturaHistoria1 = assetManager.get("Historia1.jpg");
        texturaHistoria2 = assetManager.get("Historia2.jpg");
        texturaHistoria3 = assetManager.get("Historia3.jpg");
        texturaHistoria4 = assetManager.get("Historia4.jpg");
        texturaHistoria5 = assetManager.get("Historia5.jpg");
        texturaHistoria6 = assetManager.get("Historia6.jpg");
        texturaHistoria7 = assetManager.get("Historia7.jpg");
        texturaMenu = assetManager.get("back.png");
        btnHistoria1 = new Boton(texturaHistoria1);
        btnHistoria2 = new Boton(texturaHistoria2);
        btnHistoria3 = new Boton(texturaHistoria3);
        btnHistoria4 = new Boton(texturaHistoria4);
        btnHistoria5 = new Boton(texturaHistoria5);
        btnHistoria6 = new Boton(texturaHistoria6);
        btnHistoria7 = new Boton(texturaHistoria7);
        btnMenu = new Boton(texturaMenu);
        btnMenu.setPosicion(10,10);

        //Musica de fondo
        musicFondo = Gdx.audio.newMusic(Gdx.files.internal("Principal.mp3"));
        musicFondo.setLooping(true);
        musicFondo.play();
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

        tiempoJuego += Gdx.graphics.getDeltaTime();


        if(estadoPantalla == EstadosPantalla.INICIANDO) {
            if ((int) tiempoJuego < 2) {
                btnTec.render(batch);
            } else {
                btnRegresar.render(batch);
            }
        }else if(estadoPantalla == EstadosPantalla.HISTORIA1){
            btnHistoria1.render(batch);
            btnMenu.render(batch);
        }else if(estadoPantalla == EstadosPantalla.HISTORIA2){
            btnHistoria2.render(batch);
            btnMenu.render(batch);
        }else if(estadoPantalla == EstadosPantalla.HISTORIA3){
            btnHistoria3.render(batch);
            btnMenu.render(batch);
        }else if(estadoPantalla == EstadosPantalla.HISTORIA4){
            btnHistoria4.render(batch);
            btnMenu.render(batch);
        }else if(estadoPantalla == EstadosPantalla.HISTORIA5){
            btnHistoria5.render(batch);
            btnMenu.render(batch);
        }else if(estadoPantalla == EstadosPantalla.HISTORIA6){
            btnHistoria6.render(batch);
            btnMenu.render(batch);
        }else if(estadoPantalla == EstadosPantalla.HISTORIA7){
            btnHistoria7.render(batch);
            btnMenu.render(batch);
        }else if(estadoPantalla == EstadosPantalla.FINALIZANDO){
            btnRegresar.render(batch);
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
        assetManager.unload("PantallaDeInicio.png");
        assetManager.unload("LogoTec.JPG");
//        efecto.dispose();
//        explosion.dispose();
    }

    /*
    Clase utilizada para manejar los eventos de touch en la pantalla
     */
    public class ProcesadorEntrada extends InputAdapter
    {
        private Vector3 coordenadas = new Vector3();
        private float x, y;     // Las coordenadas en la pantalla

        /*
        Se ejecuta cuando el usuario PONE un dedo sobre la pantalla, los dos primeros parámetros
        son las coordenadas relativas a la pantalla física (0,0) en la esquina superior izquierda
        pointer - es el número de dedo que se pone en la pantalla, el primero es 0
        button - el botón del mouse
         */
        @Override
        public boolean keyDown(int keycode) {
            if (keycode== Input.Keys.BACK) {
                if(estadoPantalla == EstadosPantalla.INICIANDO) {
                    System.exit(0);
                }
                else if(estadoPantalla == EstadosPantalla.HISTORIA1) {
                    estadoPantalla = EstadosPantalla.INICIANDO;
                }
                else if(estadoPantalla == EstadosPantalla.HISTORIA2) {
                    estadoPantalla = EstadosPantalla.HISTORIA1;
                }
                else if(estadoPantalla == EstadosPantalla.HISTORIA3) {
                    estadoPantalla = EstadosPantalla.HISTORIA2;
                }
                else if(estadoPantalla == EstadosPantalla.HISTORIA4) {
                    estadoPantalla = EstadosPantalla.HISTORIA3;
                }
                else if(estadoPantalla == EstadosPantalla.HISTORIA5) {
                    estadoPantalla = EstadosPantalla.HISTORIA4;
                }
                else if(estadoPantalla == EstadosPantalla.HISTORIA6) {
                    estadoPantalla = EstadosPantalla.HISTORIA5;
                }
                else if(estadoPantalla == EstadosPantalla.HISTORIA7) {
                    estadoPantalla = EstadosPantalla.HISTORIA6;
                }
                else if(estadoPantalla == EstadosPantalla.FINALIZANDO) {
                    estadoPantalla = EstadosPantalla.HISTORIA7;
                }
            }
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
            if(estadoPantalla == EstadosPantalla.INICIANDO){
                if (btnRegresar.contiene(x,y)&&tiempoJuego>2.5){
                    estadoPantalla=EstadosPantalla.HISTORIA1;
                }
            }else if(estadoPantalla == EstadosPantalla.HISTORIA1){
                if (btnHistoria1.contiene(x,y)){
                    estadoPantalla=EstadosPantalla.HISTORIA2;
                }
                if(btnMenu.contiene(x,y)){
                    musicFondo.stop();
                    musicFondo.dispose();
                    Gdx.input.setInputProcessor(null);
                    plataforma.setScreen(new PrincipalPantalla(plataforma));
                }
            }
            else if(estadoPantalla == EstadosPantalla.HISTORIA2){
                if (btnHistoria2.contiene(x,y)){
                    estadoPantalla=EstadosPantalla.HISTORIA3;
                }
                if(btnMenu.contiene(x,y)){
                    musicFondo.stop();
                    musicFondo.dispose();
                    Gdx.input.setInputProcessor(null);
                    plataforma.setScreen(new PrincipalPantalla(plataforma));
                }
            }else if(estadoPantalla == EstadosPantalla.HISTORIA3){
                if (btnHistoria3.contiene(x,y)){
                    estadoPantalla=EstadosPantalla.HISTORIA4;
                }
                if(btnMenu.contiene(x,y)){
                    musicFondo.stop();
                    musicFondo.dispose();
                    Gdx.input.setInputProcessor(null);
                    plataforma.setScreen(new PrincipalPantalla(plataforma));
                }
            }else if(estadoPantalla == EstadosPantalla.HISTORIA4){
                if (btnHistoria4.contiene(x,y)){
                    estadoPantalla=EstadosPantalla.HISTORIA5;
                }
                if(btnMenu.contiene(x,y)){
                    musicFondo.stop();
                    musicFondo.dispose();
                    Gdx.input.setInputProcessor(null);
                    plataforma.setScreen(new PrincipalPantalla(plataforma));
                }
            }else if(estadoPantalla == EstadosPantalla.HISTORIA5){
                if (btnHistoria5.contiene(x,y)){
                    estadoPantalla=EstadosPantalla.HISTORIA6;
                }
                if(btnMenu.contiene(x,y)){
                    musicFondo.stop();
                    musicFondo.dispose();
                    Gdx.input.setInputProcessor(null);
                    plataforma.setScreen(new PrincipalPantalla(plataforma));
                }
            }else if(estadoPantalla == EstadosPantalla.HISTORIA6){
                if (btnHistoria6.contiene(x,y)){
                    estadoPantalla=EstadosPantalla.HISTORIA7;
                }
                if(btnMenu.contiene(x,y)){
                    musicFondo.stop();
                    musicFondo.dispose();
                    Gdx.input.setInputProcessor(null);
                    plataforma.setScreen(new PrincipalPantalla(plataforma));
                }
            }else if(estadoPantalla == EstadosPantalla.HISTORIA7){
                if (btnHistoria7.contiene(x,y)){
                    estadoPantalla = EstadosPantalla.FINALIZANDO;
                }
                if(btnMenu.contiene(x,y)){
                    musicFondo.stop();
                    musicFondo.dispose();
                    Gdx.input.setInputProcessor(null);
                    plataforma.setScreen(new PrincipalPantalla(plataforma));
                }
            }else if (estadoPantalla == EstadosPantalla.FINALIZANDO){
                if(btnRegresar.contiene(x,y)){
                    musicFondo.stop();
                    musicFondo.dispose();
                    Gdx.input.setInputProcessor(null);
                    plataforma.setScreen(new PrincipalPantalla(plataforma));
                }
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

    public enum EstadosPantalla {
        INICIANDO,
        HISTORIA1,
        HISTORIA2,
        HISTORIA3,
        HISTORIA4,
        HISTORIA5,
        HISTORIA6,
        HISTORIA7,
        FINALIZANDO
    }
}