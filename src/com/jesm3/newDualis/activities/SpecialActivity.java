package com.jesm3.newDualis.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.jesm3.newDualis.R;

public class SpecialActivity extends Activity implements OnGestureListener {

	private SurfaceHolder holder;
	private Thread gameThread;
	private boolean isRunning;
	private Canvas canvas;
	private GestureDetector gestureScanner;

	private Paint snakePaint;
	private Paint foodPaint;
	private Paint backgroundPaint;

	private Snake dieSnake;
	private Essen dasEssen;
	private Paint scorePaint;
	private int spielGroesse = 20;
	private int score = 0;
	private int schwierigkeitsgrad = 0;
	private int feldBreite = 20;
	private int feldHoehe = 15;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.special_layout);
		gestureScanner = new GestureDetector(this);
		dasEssen = new Essen(5, 5);
		init();

		snakePaint = new Paint();
		snakePaint.setColor(Color.BLACK);
		snakePaint.setAntiAlias(true);
		snakePaint.setStyle(Paint.Style.FILL_AND_STROKE);

		foodPaint = new Paint();
		foodPaint.setColor(Color.BLACK);

		backgroundPaint = new Paint();
		backgroundPaint.setColor(getResources().getColor(R.color.mintgreen));

		scorePaint = new Paint();
		scorePaint.setTextSize(20);

		initGameThread();
		getHolder();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isRunning = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		isRunning = false;
	}

	// Setze Spiel zum Ausgangspunkt zurück
	private void init() {
		feldBreite = (int) ((SurfaceView) findViewById(R.id.surfaceView))
				.getWidth() / spielGroesse;
		feldHoehe = (int) ((SurfaceView) findViewById(R.id.surfaceView))
				.getHeight() / spielGroesse;
		dieSnake = new Snake(5, 5, feldBreite, feldHoehe, 1);
		score = 0;
		neuesEssen();
	}

	public void initGameThread() {
		isRunning = true;
		gameThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {

					// Bewege die Schlange. Wenn Kollision -> Setze Spiel
					// Zurück
					if (dieSnake.bewege()) {
						init();
					}

					/*
					 * Prüfe Kollision mit Fressen. Wenn ja -> Neue Position
					 * fürs Essen -> Verlängere die Schlange -> Erhöhe Score
					 */
					if (dieSnake.pruefeEssenKoll(dasEssen)) {
						neuesEssen();
						dieSnake.fressen();
						score += schwierigkeitsgrad / 10 + 1;
						schwierigkeitsgrad = schwierigkeitsgrad < 50 ? schwierigkeitsgrad + 1
								: schwierigkeitsgrad;
					}

					canvas = null;
					try {
						canvas = getHolder().lockCanvas();
						synchronized (getHolder()) {
							if (canvas != null) {
								repaint(canvas);
							}
						}
					} finally {
						if (canvas != null) {
							getHolder().unlockCanvasAndPost(canvas);
						}
					}

					// Halte den Spielablauf abhängig von der Schwierigkeit an
					try {
						Thread.sleep(150 - schwierigkeitsgrad * 2);
					} catch (InterruptedException ex) {
					}
				}
			}
		});
	}

	public void repaint(Canvas aCanvas) {
		// Mache das ganze Bild Schwarz
		aCanvas.drawColor(Color.BLACK);

		aCanvas.drawRect(spielGroesse, spielGroesse, spielGroesse
				* (feldBreite - 1), spielGroesse * (feldHoehe - 1),
				backgroundPaint);

		// Zeichne Schlangenkopf
		aCanvas.drawRect(dieSnake.getKopfXPos() * spielGroesse,
				dieSnake.getKopfYPos() * spielGroesse, dieSnake.getKopfXPos()
						* spielGroesse + spielGroesse, dieSnake.getKopfYPos()
						* spielGroesse + spielGroesse, snakePaint);
		// Zeichne alle K�rperteile der Schlange
		for (Koerperteil k : dieSnake.getKoerper()) {
			aCanvas.drawRect(k.getxPos() * spielGroesse, k.getyPos()
					* spielGroesse, k.getxPos() * spielGroesse + spielGroesse,
					k.getyPos() * spielGroesse + spielGroesse, snakePaint);
		}

		// Zeichne Essen
		aCanvas.drawRect(dasEssen.getxPos() * spielGroesse, dasEssen.getyPos()
				* spielGroesse, dasEssen.getxPos() * spielGroesse
				+ spielGroesse, dasEssen.getyPos() * spielGroesse
				+ spielGroesse, foodPaint);

		// Zeichne Score
		aCanvas.drawText("Score: " + score + "", 5 + spielGroesse,
				20 + spielGroesse, scorePaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureScanner.onTouchEvent(me);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float absVX = velocityX * velocityX;
		float absVY = velocityY * velocityY;
		if (absVX > absVY) {
			if (velocityX < 0) {
				dieSnake.setRichtung(3);
			} else {
				dieSnake.setRichtung(1);
			}
		} else {
			if (velocityY < 0) {
				dieSnake.setRichtung(0);
			} else {
				dieSnake.setRichtung(2);
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return true;
	}

	public SurfaceHolder getHolder() {
		if (holder == null) {
			holder = ((SurfaceView) findViewById(R.id.surfaceView)).getHolder();
			holder.addCallback(new SurfaceHolder.Callback() {
				@Override
				public void surfaceCreated(SurfaceHolder surfaceHolder) {
					init();
					gameThread.start();
				}

				@Override
				public void surfaceChanged(SurfaceHolder surfaceHolder, int i,
						int i2, int i3) {
					// To change body of implemented methods use File | Settings
					// | File Templates.
				}

				@Override
				public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
					// To change body of implemented methods use File | Settings
					// | File Templates.
				}
			});
		}
		return holder;
	}

	// Setze neue Position für dasEssen. Position wird zufällig bestimmt bis
	// die Position nicht mit der Schlange kollidiert
	public void neuesEssen() {
		do {
			dasEssen.setxPos((int) (Math.random() * (feldBreite - 2)) + 1);
			dasEssen.setyPos((int) (Math.random() * (feldHoehe - 2)) + 1);
		} while (dieSnake.pruefeEssenKoll(dasEssen));
	}

	// Verarbeite Tastatureingabe
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent evt) {
		if (keyCode == KeyEvent.KEYCODE_W) {
			setRichtung(dieSnake, 0);
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			setRichtung(dieSnake, 2);
		} else if (keyCode == KeyEvent.KEYCODE_D) {
			setRichtung(dieSnake, 1);
		} else if (keyCode == KeyEvent.KEYCODE_A) {
			setRichtung(dieSnake, 3);
		}
		return super.onKeyDown(keyCode, evt);
	}

	// Verändere die Bewegungsrichtung der Schlange wenn nicht im Menü
	public void setRichtung(Snake snake, int richtung) {
		snake.setRichtung(richtung);
	}

	private class Essen {
		private int xPos;
		private int yPos;

		public Essen(int xPos, int yPos) {
			this.xPos = xPos;
			this.yPos = yPos;
		}

		public int getxPos() {
			return xPos;
		}

		public void setxPos(int xPos) {
			this.xPos = xPos;
		}

		public int getyPos() {
			return yPos;
		}

		public void setyPos(int yPos) {
			this.yPos = yPos;
		}
	}

	public class Koerperteil {

		private int xPos;
		private int yPos;

		public Koerperteil(int xPos, int yPos) {
			this.xPos = xPos;
			this.yPos = yPos;
		}

		public int getxPos() {
			return xPos;
		}

		public void setxPos(int xPos) {
			this.xPos = xPos;
		}

		public int getyPos() {
			return yPos;
		}

		public void setyPos(int yPos) {
			this.yPos = yPos;
		}
	}

	public class Snake {
		private int kopfXPos;
		private int kopfYPos;
		private int richtung;
		private int feldBreite;
		private int feldHoehe;
		private boolean hatGefressen;
		private ArrayList<Koerperteil> koerper;

		public Snake(int kopfXPos, int kopfYPos, int breite, int hoehe, int richtung) {
			this.richtung = richtung;
			this.kopfXPos = kopfXPos;
			this.kopfYPos = kopfYPos;

			this.feldBreite = breite;
			this.feldHoehe = hoehe;

			koerper = new ArrayList<Koerperteil>();
			koerper.add(new Koerperteil(kopfXPos + richtung - 2, kopfYPos));
			koerper.add(new Koerperteil(kopfXPos + richtung - 3, kopfYPos));
		}

		// Ausf�hren einer Bewegung
		public boolean bewege() {
			// F�r jedes K�rperteil
			for (int i = koerper.size() - 1; i >= 0; i--) {
				// Wenn ein Essen gefressen wurde
				if (hatGefressen) {
					// F�ge neues K�rperteil am Ende hinzu
					koerper.add(new Koerperteil(koerper.get(i).getxPos(),
							koerper.get(i).getyPos()));
					hatGefressen = false;
				}

				// Setze jedes K�rperteil zu der Position des K�rperteils
				// welches 1 n�her am Kopf ist
				if (i > 0) {
					koerper.get(i).setxPos(koerper.get(i - 1).getxPos());
					koerper.get(i).setyPos(koerper.get(i - 1).getyPos());
				} else {
					// Setze K�rperteil hinterm Kopf zur Position des Kopfes
					koerper.get(i).setxPos(kopfXPos);
					koerper.get(i).setyPos(kopfYPos);
				}
			}
			// Ver�ndere die Position des Kopfes je nach Richtung
			switch (richtung) {
			case 0: // Oben
				kopfYPos--;
				break;

			case 1: // Rechts
				kopfXPos++;
				break;

			case 2: // Unten
				kopfYPos++;
				break;

			case 3: // Links
				kopfXPos--;
				break;
			}

			// �berpr�fue Kollision mit den W�nden
			if (kopfYPos < 1 || kopfXPos < 1 || kopfYPos >= feldHoehe - 1
					|| kopfXPos >= feldBreite - 1) {
				return true;
			}
			return checkKoerperKoll(kopfXPos, kopfYPos);
		}

		public boolean checkKoerperKoll(int xPos, int yPos) {
			// �berpr�fe Kollision des Kopfes mit jedem K�rperteil
			for (Koerperteil k : koerper) {
				if (xPos == k.getxPos() && yPos == k.getyPos()) {
					return true;
				}
			}
			return false;
		}

		// �berpr�fe Kollision der Schlange mit Essen
		public boolean pruefeEssenKoll(Essen essen) {
			for (Koerperteil k : koerper) {
				if (k.getxPos() == essen.getxPos()
						&& k.getyPos() == essen.getyPos()) {
					return true;
				}
			}
			if (kopfXPos == essen.getxPos() && kopfYPos == essen.getyPos()) {
				return true;
			}
			return false;
		}

		public void fressen() {
			hatGefressen = true;
		}

		public ArrayList<Koerperteil> getKoerper() {
			return koerper;
		}

		public Point gibKopf() {
			return new Point(kopfXPos, kopfYPos);
		}

		public int getKopfXPos() {
			return kopfXPos;
		}

		public int getKopfYPos() {
			return kopfYPos;
		}

		// Setze die Richtung. Die Richtung darf nicht entgegengesetzt werden.
		public void setRichtung(int richtung) {
			if (richtung % 2 == 0 && this.richtung % 2 != 0
					|| richtung % 2 != 0 && this.richtung % 2 == 0) {
				this.richtung = richtung;
			}
		}
	}
}
