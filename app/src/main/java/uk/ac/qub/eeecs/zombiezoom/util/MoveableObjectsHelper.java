package uk.ac.qub.eeecs.zombiezoom.util;

public final class MoveableObjectsHelper {

	/**
	 * Determine the displacement the object can travel using the equation
	 * s=(v^2-u^2)/2a
	 * 
	 * @param v
	 *            final velocity
	 * @param u
	 *            initial velocity
	 * @param a
	 *            acceleration
	 * @return displacement
	 */
	public static final float calculateDisplacementWithAcceleration(float v,
			float u, float a) {
		return (float) ((Math.pow(v, 2) - Math.pow(u, 2)) / (2 * a));
	}

	/**
	 * Determine the displacement the object can travel using the equation s =
	 * ((u + v)/2)t
	 * 
	 * @param v
	 *            final velocity
	 * @param u
	 *            initial velocity
	 * @param t
	 *            time
	 * @return displacement
	 */
	public static final float calculateDisplacementWithTime(float v, float u,
			float t) {
		return (float) ((u + v) / 2) * t;
	}

	/**
	 * Determine the displacement the object can travel when the initial
	 * velocity is equal to the final velocity i.e. u==v using the equation s =
	 * ((u + u)/2)t
	 * 
	 * @param u
	 *            initial velocity
	 * @param t
	 *            time
	 * @return displacement
	 */
	public static final float calculateDisplacementWithTime(float u, float t) {
		return (float) u * t;
	}

	/**
	 * 
	 * Determine the time the object takes to travel using the equation
	 * t=(v-u)/a
	 * 
	 * @param v
	 *            final velocity
	 * @param u
	 *            initial velocity
	 * @param a
	 *            acceleration
	 * @return positive time
	 */
	public static final float calculateTime(float v, float u, float a) {
		return (float) Math.abs((v - u) / a);
	}

	/**
	 * 
	 * Determine the time the object takes to travel using the equation t^2=2s/a
	 * 
	 * @param s
	 *            displacement
	 * @param u
	 *            initial velocity
	 * @param a
	 *            acceleration
	 * @return positive time
	 */
	public static final float calculateTimeWithAnInitalVelocityOfZero(float s,
			float a) {
		return (float) Math.sqrt((2 * s) / a);
	}
}