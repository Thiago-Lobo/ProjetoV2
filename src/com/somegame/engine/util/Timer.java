package com.ioavthiago.engine.util;

/*
 * Temporizador
 * Utilizado para controlar eventos periódicos. Deve ser inicializado com o número de vezes
 * por segundo que algo acontece (frequência em Hz).
 * Deve ser utilizado num "loop infinito". A cada loop, deve-se pedir permissão ao timer com
 * timePassed(). Caso o timer retorne true, pode-se executar a ação periódica.
 */

public class Timer {

	private final float ns = 1000000000f;
	private long now;
	private long then;
	private float difference;
	
	public Timer(float timesPerSecond) {
		difference = ns/timesPerSecond;
		start();
	}
	
	/*
	 * Inicializador (resetador) do timer.
	 */
	public void start() {
		now = System.nanoTime();
	}
	
	/*
	 * Permissão para executar.
	 * Checa se o tempo programado passou desde o último pedido.
	 */
	public boolean timePassed() {
		then = System.nanoTime();
		if (then - now	>= difference) {
			now = then;
			return true;
		}
		
		return false;
	}
	
}
