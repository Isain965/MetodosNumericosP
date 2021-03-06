package mx.izo.metodos;

/**
 * Created by isain on 20/04/2017.
 */
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by isain on 10/11/2016.
 */

public class Boton {

    private Sprite sprite;              // Imagen
    private Rectangle rectColision;     // Rectangulo para verificar colisiones o touch

    public Boton(Texture textura) {
        sprite = new Sprite(textura);
        // El rectángulo de colisión siempre está 'sobre' el sprite
        rectColision = new Rectangle(sprite.getX(), sprite.getY(),
                sprite.getWidth(), sprite.getHeight());
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x, y);
        rectColision.setPosition(x,y);
    }

    public Rectangle getRectColision() {
        return rectColision;
    }

    // Dibuja el botón
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getY() {
        return sprite.getY();
    }

    public float getX() {
        return sprite.getX();
    }

    public void setAlfa(float alfa) {
        sprite.setAlpha(alfa);
    }

    public boolean contiene(float x, float y) {
        return rectColision.contains(x,y);
    }
}