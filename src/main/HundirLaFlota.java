package main;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import barcos.*;

/**
 * Juego de hundir la flota con POO. Un jugador despliega la flota de barcos y
 * el otro jugador intenta hundir la flota en un límite de turnos. La partida
 * acaba cuando se hunde la flota o se acaba el límite de turnos disponibles
 * para hundirla
 * 
 * @author Ricardo Bordería Pi
 *
 */
public class HundirLaFlota {

	private static Scanner sc;

	/**
	 * Tablero del juego
	 */
	private static Barco[][] tablero;

	/**
	 * HashSet con los barcos hundidos
	 */
	private static Set<Barco> barcosHundidos;

	/**
	 * Tablero informativo para el atacante
	 */
	private static char[][] tableroSolucion;
	/**
	 * Tablero informativo para el que despliega la flota
	 */
	private static char[][] tableroCreacion;

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		tableroSolucion = new char[10][10];
		tableroCreacion = new char[10][10];
		cargarTablero(tableroSolucion);
		cargarTablero(tableroCreacion);
		tablero = new Barco[10][10];
		barcosHundidos = new HashSet<Barco>();

		Barco p = new Portaaviones();
		crearBarco(p);
		mostrarTablero(tableroCreacion);

//		Barco s1 = new Submarino();
//		crearBarco(s1);
//		mostrarTablero(tableroCreacion);
//		Barco s2 = new Submarino();
//		crearBarco(s2);
//		mostrarTablero(tableroCreacion);

		Barco f1 = new Fragata();
		crearBarco(f1);
		mostrarTablero(tableroCreacion);
		Barco f2 = new Fragata();
		crearBarco(f2);
		mostrarTablero(tableroCreacion);

//		Barco l1 = new Lancha();
//		crearBarco(l1);
//		mostrarTablero(tableroCreacion);
//		Barco l2 = new Lancha();
//		crearBarco(l2);
//		mostrarTablero(tableroCreacion);

		esconderTablero();
		int turno = 0;
		int turnosTotales = 20;
		mostrarTablero(tableroSolucion);
		while (barcosHundidos.size() < 7 && turno < turnosTotales) {
			disparar();
			turno++;
		}
		if (turno < turnosTotales)
			System.out.println("Victoria");
		else
			System.out.println("Derrota. Se acabó el límite de tiempo.");
	}

	/**
	 * Esconde el tableroCreacion para que el atacante no lo vea
	 */
	private static void esconderTablero() {
		for (int i = 0; i < 20; i++) {
			System.out.println("");
		}
	}

	/**
	 * Procesa un disparo
	 */
	private static void disparar() {
		// Solicito las coordenadas del ataque
		System.out.println("Introduce la coordenada x [0,9] del disparo: ");
		int x = Integer.parseInt(sc.nextLine());
		System.out.println("Introduce la coordenada y [0,9] del disparo: ");
		int y = Integer.parseInt(sc.nextLine());
		// Si las coordenadas está en el tablero realizo el disparo
		if (x >= 0 && x < 10 && y >= 0 && y < 10) {
			String res = disparo(x, y);
			switch (res) {
			// Si el barco ha sido tocado pinto una x en el tableroSolucion
			case "Tocado":
				tableroSolucion[y][x] = 'X';
				break;
			// Si el disparo ha sido Agua pinto una A en la posición del disparo
			case "Agua":
				tableroSolucion[y][x] = 'A';
			}
			System.out.println(res);
			// Si las coordenadas están fuera del tablero informo al atacante
		} else
			System.out.println("Coordenada fuera del tablero.");
		mostrarTablero(tableroSolucion);
	}

	/**
	 * Imprime el tablero para orientar a los jugadores
	 * 
	 * @param tablero tablero a imprimir
	 */
	private static void mostrarTablero(char[][] tablero) {
		System.out.println("  0 1 2 3 4 5 6 7 8 9 X");
		for (int i = 0; i < tablero.length; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < tablero.length; j++) {
				System.out.print(tablero[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("Y");
	}

	/**
	 * Carga el tablero informativo en su aspecto inicial
	 * 
	 * @param tablero tablero informativo para inicializar
	 */
	private static void cargarTablero(char[][] tablero) {
		for (int i = 0; i < tablero.length; i++) {
			for (int j = 0; j < tablero.length; j++) {
				tablero[i][j] = '*';
			}
		}
	}

	/***
	 * Intenta desplegar un barco
	 * 
	 * @param b barco a desplegar
	 */
	private static void crearBarco(Barco b) {
		boolean seguir = true;
		do {
			// Solicito las coorenadas iniciales y la dirección en la que se desea desplegar
			// el barco
			System.out.println("Introduce la coordenada x inicial [0, 9]: ");
			int x = Integer.parseInt(sc.nextLine());
			System.out.println("Introduce la coordenada y inicial [0, 9]: ");
			int y = Integer.parseInt(sc.nextLine());
			System.out.println("Introduce la dirección: ");
			String dir = sc.nextLine();
			// Intento crear el barco
			String mensaje = b.setCoordenadas(x, y, dir);
			// Si el barco se ha creado se ha desplegado con éxito compruebo qe no se
			// superponga con otro barco existente
			if (mensaje.equals("Barco añadido")) {
				seguir = llenarTablero(b);
				if (seguir)
					System.out.println("Hay barcos solapados. Introduce coordenadas nuevas.");
			} else
				System.out.println(mensaje);
			if (!seguir)
				System.out.println(mensaje);
		} while (seguir);
	}

	/**
	 * Intento incluir el barco al tablero
	 * 
	 * @param b barco a incluir
	 * @return false si el barco se ha creado correctamente, true de lo contrario
	 */
	private static boolean llenarTablero(Barco b) {
		// Intuyo que el barco se desplegará correctamente
		boolean correto = true;
		// Compruebo si alguna coordenada del barco está encima de otro barco
		for (int i = 0; i < b.getX().length; i++) {
			int x = b.getX()[i];
			int y = b.getY()[i];
			if (tablero[y][x] instanceof Barco)
				correto = false;
		}
		// Si no se superpone con ningún barco lo añado al tablero y al tableroCreación
		if (correto) {
			for (int i = 0; i < b.getX().length; i++) {
				int x = b.getX()[i];
				int y = b.getY()[i];
				tablero[y][x] = b;
				tableroCreacion[y][x] = 'X';
			}
			return false;
		} else
			return true;
	}

	/**
	 * Proceso el disparo
	 * 
	 * @param x coordenada x del disparo
	 * @param y coordenada y del disparo
	 * @return resultado de la operación
	 */
	private static String disparo(int x, int y) {
		String mensaje = "Agua";
		// Cojo el barco en la posición del disparo
		Barco barco = tablero[y][x];
		// Si hay un barco disparo al barco
		if (barco != null) {
			if (barco.getHundido())
				mensaje = "El barco ya está hundido";
			else {
				if (barco.getEstado().containsKey(x + " " + y))
					mensaje = barco.comprobarTiro(x, y);
				if (barco.getHundido())
					barcosHundidos.add(barco);
			}
		}
		return mensaje;
	}
}
