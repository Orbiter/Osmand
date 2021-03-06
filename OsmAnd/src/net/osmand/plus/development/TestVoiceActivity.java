/**
 * 
 */
package net.osmand.plus.development;

import net.osmand.access.AccessibleToast;
import net.osmand.plus.OsmandApplication;
import net.osmand.plus.R;
import net.osmand.plus.voice.AbstractPrologCommandPlayer;
import net.osmand.plus.voice.CommandBuilder;
import net.osmand.plus.voice.CommandPlayer;
import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * Test Voice activity
 */
public class TestVoiceActivity extends SherlockActivity {



	@Override
	public void onCreate(Bundle icicle) {
		((OsmandApplication) getApplication()).applyTheme(this);
		super.onCreate(icicle);
		getSherlock().setUiOptions(ActivityInfo.UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		final OsmandApplication app = ((OsmandApplication) getApplication());
		
		
		LinearLayout gl = new LinearLayout(this);
		gl.setOrientation(LinearLayout.VERTICAL);
		gl.setPadding(3, 3, 3, 3);
		
		TextView tv = new TextView(this);
		tv.setText("Press buttons and listen various voice instructions, if you don't hear anything probably they are missed.");
		tv.setPadding(0, 5, 0, 7);
		
		ScrollView sv = new ScrollView(this);
		gl.addView(sv, new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
		final LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll, new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
		
		// add buttons
		setContentView(gl);
		getSupportActionBar(). setTitle(R.string.test_voice_prompts);
		
		Runnable r = new Runnable(){

			@Override
			public void run() {
				CommandPlayer p = app.getRoutingHelper().getVoiceRouter().getPlayer();
				if (p == null) {
					AccessibleToast.makeText(TestVoiceActivity.this, "Voice player not initialized", Toast.LENGTH_SHORT).show();
				} else {
					addButtons(ll, p);
				}
			}
			
		};
		if (app.getRoutingHelper().getVoiceRouter().getPlayer() != null) {
			r.run();
		} else {
			app.showDialogInitializingCommandPlayer(this, true, r);
		}
	}
	
	private void addButtons(final LinearLayout ll, CommandPlayer p) {
		addButton(ll, "New route was calculated (15350m & 2h30m5sec)", builder(p).newRouteCalculated(15350, 9005));
		addButton(ll, "After 1050m turn slightly left 'to Strasse'", builder(p).turn(AbstractPrologCommandPlayer.A_LEFT_SL, 1050, "Strasse"));
		addButton(ll, "Turn left 'to Street'", builder(p).turn(AbstractPrologCommandPlayer.A_LEFT, "Street"));
		addButton(ll, "Prepare to turn right after 320m 'to Mini'", builder(p).prepareTurn(AbstractPrologCommandPlayer.A_RIGHT, 320, "Mini"));
		addButton(ll, "After 370m turn sharply right 'to F23'", builder(p).turn(AbstractPrologCommandPlayer.A_RIGHT_SH, 370, "F23"));
		addButton(ll, "Prepare to turn slighlty left after 850m then bear right", builder(p).prepareTurn(AbstractPrologCommandPlayer.A_LEFT_SL, 850, "").then().bearRight(""));
		addButton(ll, "Turn sharply right 'to Street' then bear left ", builder(p).turn(AbstractPrologCommandPlayer.A_RIGHT_SH, "Street").then().bearLeft(""));
		addButton(ll, "Prepare to make a U-turn after 400m", builder(p).prepareMakeUT(400, ""));
		addButton(ll, "After 640m make a U-turn ", builder(p).makeUT(640, ""));
		addButton(ll, "Prepare to keep left '' after 370m", builder(p).prepareTurn(AbstractPrologCommandPlayer.A_LEFT_KEEP, 370, ""));
		addButton(ll, "Keep left '' then after 400m keep right 'A1'", builder(p).turn(AbstractPrologCommandPlayer.A_LEFT_KEEP, "").then().turn(AbstractPrologCommandPlayer.A_RIGHT_KEEP, 400, "A1"));
		addButton(ll, "Make a U-turn 'to Riviera' ", builder(p).makeUT("Riviera"));
		addButton(ll, "When possible, make a U-turn", builder(p).makeUTwp());
		addButton(ll, "Prepare to enter a roundabout (3rd exit) 'Liberty' after 750m", builder(p).prepareRoundAbout(750, 3, "Liberty"));
		addButton(ll, "After 450m enter the roundabout and take the 1st exit 'to Place'", builder(p).roundAbout(450, 0, 1, "Place"));
		addButton(ll, "Roundabout: Take the 2nd exit 'to Bridge Avenue'", builder(p).roundAbout(0, 2, "Bridge Avenue"));
		addButton(ll, "GPS signal lost", builder(p).gpsLocationLost());
		addButton(ll, "Route recalculated (23150m & 350sec)", builder(p).routeRecalculated(23150, 350));
		addButton(ll, "Follow the '' road for 2350m", builder(p).goAhead(2350, ""));
		addButton(ll, "Follow the road 'Street' for 360m and arrive at waypoint", builder(p).goAhead(360, "Street").andArriveAtIntermediatePoint(""));
		addButton(ll, "Arrive at intermediate point 'Friend'", builder(p).arrivedAtIntermediatePoint("Friend"));
		addButton(ll, "Follow the road 'A33' for 800m and arrive at destination", builder(p).goAhead(800, "A33").andArriveAtDestination(""));
		addButton(ll, "Arrive at destination 'Home'", builder(p).arrivedAtDestination("Home"));
		addButton(ll, "Arrive at waypoint 'GPX point'", builder(p).arrivedAtWayPoint("GPX point"));
		addButton(ll, "You are off the route for 1050m", builder(p).offRoute(1050));
		addButton(ll, "You exceed speed limit", builder(p).speedAlarm());
		addButton(ll, "Attention (speed camera) /bump...", builder(p).attention("speed camera"));
		ll.forceLayout();
	}
	
	
	
	private CommandBuilder builder(CommandPlayer p){
		return p.newCommandBuilder();
	}
	
	
	public void addButton(ViewGroup layout, String description, final CommandBuilder builder){
		Button button = new Button(this);
		button.setText(description);
		button.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		button.setPadding(10, 5, 10, 2);
		
		layout.addView(button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				builder.play();
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case android.R.id.home:
			finish();
			return true;

		}
		return false;
	}

}
