package net.towerdefender;

import java.util.Iterator;
import gameplay.Enemie;
import gameplay.Game;
import gameplay.GameConfig;
import gameplay.Position;
import gameplay.Tower;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class TowerDefender extends Activity {

	Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		game = new Game();
		game.start();

		// setContentView(R.layout.activity_tower_defender);
		TowerDefenderView towerDefenderView = new TowerDefenderView(this);
		setContentView(towerDefenderView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tower_defender, menu);
		return true;
	}

	public class TowerDefenderView extends View {
		public TowerDefenderView(Context context) {
			super(context);
		}

		@SuppressLint("DrawAllocation")
		@Override
		public void onDraw(Canvas canvas) {
			
			
			
			canvas.drawColor(Color.BLACK);
			canvas.scale(5, 5);

			Iterator<Position> ite = GameConfig.defaulPath.getPath().iterator();
			Position old = ite.next();
			while (ite.hasNext()) {
				Position pos = ite.next();
				drawLine(canvas, old, pos, Color.GRAY);
				old = pos;
			}

			for (Tower tower : game.getTowers()) {
				drawOval(canvas, new RectF(tower.getPosition().getPositionX(),
						tower.getPosition().getPositionY(), 10, 10), Color.BLUE);

				int color = 0;
				if (tower.targetedEnemieInRange())
					color = Color.MAGENTA;
				else
					color = Color.CYAN;

				if (tower.getTarget() != null) {
					if (tower.getTarget().isAlive()) {

						drawLine(canvas, tower.getTarget().getPosition(),
								tower.getPosition(), color);
					}
				}
			}

			for (Enemie enemi : game.getCurrentWave().getEnemiesAlive()) {
				drawOval(canvas, new RectF(enemi.getPosition().getPositionX(),
						enemi.getPosition().getPositionY(), 10, 10), Color.RED);
			}

			drawOval(canvas, new RectF((int) game.getObjectiveEnemie()
					.getPositionX(), (int) game.getObjectiveEnemie()
					.getPositionY(), 10, 10), Color.GREEN);
			
			drawOval(canvas, new RectF((int) game.getStartPointEnemie()
					.getPositionX(), (int) game.getStartPointEnemie()
					.getPositionY(), 10, 10), Color.BLACK);
			
			invalidate();
			
			if (game.isPlaying() && !game.isPaused()) {
				game.gameTick();

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		public void drawOval(Canvas canvas, RectF position, int color) {
			Paint p = new Paint();
			// smooths
			p.setAntiAlias(true);
			p.setColor(color);
			p.setStyle(Paint.Style.STROKE);
			p.setStrokeWidth(1f);
			// opacity
			p.setAlpha(255); //

			canvas.drawOval(new RectF(position.left - (position.right / 2),
					position.top - (position.bottom / 2), position.left
							+ (position.right / 2), position.top
							+ (position.bottom / 2)), p);

		}

		public void drawLine(Canvas canvas, Position start, Position end,
				int color) {
			Paint p = new Paint();
			// smooths
			p.setAntiAlias(true);
			p.setColor(color);
			p.setStyle(Paint.Style.STROKE);
			p.setStrokeWidth(1f);
			// opacity
			p.setAlpha(255); //

			canvas.drawLine(start.getPositionX(), start.getPositionY(),
					end.getPositionX(), end.getPositionY(), p);
		}
	}

}
