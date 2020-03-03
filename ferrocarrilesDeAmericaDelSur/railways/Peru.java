package ferrocarrilesDeAmericaDelSur.railways;

import ferrocarrilesDeAmericaDelSur.errors.RailwaySystemError;
import ferrocarrilesDeAmericaDelSur.errors.SetUpError;
import ferrocarrilesDeAmericaDelSur.tools.Clock;
import ferrocarrilesDeAmericaDelSur.tools.Delay;

/**
 * An implementation of a railway.  The runTrain method, should, in collaboration with Bolivia's runTrain(), guarantee
 * safe joint operation of the railways.
 */
public class Peru extends Railway {
	/**
	 * Change the parameters of the Delay constructor in the call of the superconstructor to
	 * change the behaviour of this railway.
	 * @throws SetUpError if there is an error in setting up the delay.
	 */

	public boolean hasPriority;
	public Peru() throws SetUpError {
		super("Peru",new Delay(0.1,0.3));
	}

    /**
     * Run the train on the railway.
	 * This method currently does not provide any synchronisation to avoid two trains being in the pass at the same time.
     */
	public void runTrain() throws RailwaySystemError {
		Clock clock = getRailwaySystem().getClock();
		Railway nextRailway = getRailwaySystem().getNextRailway(this);
		while (!clock.timeOut()){
			//Progress the train to the start of the pass - the waiting point
			choochoo();
			//Mark it so that Peru wishes to cross
			getBasket().putStone();
			//Run the next piece of code for as long as "Bolivia" wants to cross
			while(nextRailway.getBasket().hasStone()){
				//As long as there is a stone in the shared basket, Peru has "priority"
				//By default, Bolivia has priority as the shared basket is empty.
    			if (!getSharedBasket().hasStone()){
    				//Revoke intention
					getBasket().takeStone();
					//Do nothing for as long as Peru has priority.
    				while (!getSharedBasket().hasStone());
    				getBasket().putStone();
			}
		}
			//Critical section, cross the pass
		crossPass();
			//Switch priority to Bolivia
		getSharedBasket().takeStone();
		//Revoke intention
			getBasket().takeStone();

		}
	}
}