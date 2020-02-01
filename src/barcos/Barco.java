package barcos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Clase con las funcionalidades de la flota de barcos
 * 
 * @author Ricardo Bordería Pi
 *
 */
public abstract class Barco {

	/**
	 * Array con las coordenadas x de los barcos
	 */
	protected int[] x;
	/**
	 * Array con las coordenadas y de los barcos
	 */
	protected int[] y;

	/**
	 * Estado de los barcos. La clave son las coordeadas del barco y el valor el
	 * estado del barco en esa coordenada
	 */
	protected Map<String, String> estado = new HashMap<String, String>();

	/**
	 * Si el barco está hundido
	 */
	protected boolean hundido = false;

	/**
	 * Intenta crear un barco e informa al jugador del resultado obtenido
	 * 
	 * @param x   coordenada x inicial del barco
	 * @param y   coordenada y inicial del barco
	 * @param dir dirección de despliegue del barco
	 * @return mensaje con el resultado de la operación
	 */
	public String setCoordenadas(int x, int y, String dir) {
		// Supongo que el barco se crea con éxito
		String mensaje = "Barco añadido";
		// Si todo va bien continuo con el proceso
		boolean seguir = true;
		// Inicializo los arrays de coordenadas según la dirección de despliegue elegida
		for (int i = 0; i < this.x.length && seguir; i++) {
			switch (dir.toLowerCase()) {
			case "arriba":
				this.y[i] = y--;
				this.x[i] = x;
				break;
			case "abajo":
				this.y[i] = y++;
				this.x[i] = x;
				break;
			case "derecha":
				this.y[i] = y;
				this.x[i] = x++;
				break;
			case "izquierda":
				this.y[i] = y;
				this.x[i] = x--;
				break;
			case "arriba izquierda":
				this.y[i] = y--;
				this.x[i] = x--;
				break;
			case "arriba derecha":
				this.y[i] = y--;
				this.x[i] = x++;
				break;
			case "abajo izquierda":
				this.y[i] = y++;
				this.x[i] = x--;
				break;
			case "abajo derecha":
				this.y[i] = y++;
				this.x[i] = x++;
				break;
			default:
				mensaje = "Disposición incorrecta";
				break;
			}
			// Compruebo si las coordenadas se salen del tablero
			if (x < 0 || y < 0 || x > 9 || y > 9) {
				this.x = new int[this.x.length];
				this.y = new int[this.y.length];
				seguir = false;
				mensaje = "El barco se sale del mapa. Cambia las coordenadas iniciales o la direción del barco";
			}
		}
		// si todo va bien cargo el estado del barco
		if (seguir)
			cargarEstado();
		return mensaje;
	}

	/**
	 * Inicializo el HashMap con el estado del barco
	 */
	private void cargarEstado() {
		for (int i = 0; i < x.length; i++) {
			String clave = this.x[i] + " " + this.y[i];
			estado.put(clave, "entero");
		}
	}

	/**
	 * proceso el disparo del atacante
	 * 
	 * @param x coordenada x del ataque
	 * @param y coordenada y del ataque
	 * @return mensaje con el resultado del disparo
	 */
	public String comprobarTiro(int x, int y) {
		String mensaje = "Tocado";
		if (estado.get(x + " " + y).equals("tocado"))
			mensaje = "Agua ";
		// Si no está tocado actualizo el estado del barco
		if (estado.get(x + " " + y).equals("entero"))
			estado.put(x + " " + y, "tocado");
		// Compruebo si el barco está hundido
		boolean hundido = true;
		for (Entry<String, String> estring : this.estado.entrySet()) {
			if (!estring.getValue().equals("tocado"))
				hundido = false;
		}
		// si el barco está hundido actualizo hundido y cambio el mensaje con el
		// resultado
		if (hundido) {
			this.hundido = true;
			mensaje = "Barco hundido";
		}
		return mensaje;
	}

	/**
	 * Getter de las coordenadas x del barco
	 * 
	 * @return array con las coordenadas x del barco
	 */
	public int[] getX() {
		return x;
	}

	/**
	 * Getter de las coordenadas y del barco
	 * 
	 * @return array con las coordenadas y del barco
	 */
	public int[] getY() {
		return y;
	}

	/**
	 * Getter que indica si el barco está huundido
	 * 
	 * @return si el barco está hundido o no
	 */
	public boolean getHundido() {
		return hundido;
	}

	/**
	 * Getter del estado del barco
	 * 
	 * @return HashMap con el estado completo del barco
	 */
	public Map<String, String> getEstado() {
		return estado;
	}

	@Override
	public String toString() {
		return "Barco [x=" + Arrays.toString(x) + ", y=" + Arrays.toString(y) + ", estado=" + estado + ", hundido="
				+ hundido + "]";
	}
}
