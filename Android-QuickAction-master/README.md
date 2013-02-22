Android-QuickAction
===================
This widget provides the functionality implemented in the GreenDroid library:
https://github.com/cyrilmottier/GreenDroid

Which wasn't updated for the last 2 years. Including the library with ActionBarSherlock caused a problem so i decided to extract the QuickAction menu functionality into this repository.

Samples of how to use the library are available at the original repo. But here's a quick code snippet:

<code>
	public class QuickActionActivity extends SherlockActivity implements OnClickListener {

	    private QuickActionWidget mBar;
	    private QuickActionWidget mGrid;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        Button btn = (Button) findViewById(R.id.btnShowGrid);
	        Button btn2 = (Button) findViewById(R.id.btnShowBar);

	    	btn.setOnClickListener(this);
	    	btn2.setOnClickListener(this);

	        prepareQuickActionBar();
	        prepareQuickActionGrid();

	    }

	    @Override
	    public void onClick(View view) {
	    	switch(view.getId()) {
	    		case R.id.btnShowGrid:
	    			onShowGrid(view);
	    			break;
	    		case R.id.btnShowBar:
	    			onShowBar(view);
	    			break;
	    	}
	    }

	    public void onShowGrid(View v) {
	        mGrid.show(v);
	    }

	    public void onShowBar(View v) {
	        mBar.show(v);
	    }

	    private void prepareQuickActionBar() {
	        mBar = new QuickActionBar(this);
	        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_compose, R.string.gd_compose));
	        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_export, R.string.gd_export));
	        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_share, R.string.gd_share));

	        mBar.setOnQuickActionClickListener(mActionListener);
	    }

	    private void prepareQuickActionGrid() {
	        mGrid = new QuickActionGrid(this);
	        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_compose, R.string.gd_compose));
	        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_export, R.string.gd_export));
	        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_share, R.string.gd_share));
	        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_search, R.string.gd_search));
	        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_edit, R.string.gd_edit));
	        mGrid.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_locate, R.string.gd_locate));

	        mGrid.setOnQuickActionClickListener(mActionListener);
	    }

	    private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
	        public void onQuickActionClicked(QuickActionWidget widget, int position) {
	            Toast.makeText(QuickActionActivity.this, "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
	        }
	    };
	    
	    private static class MyQuickAction extends QuickAction {
	        
	        private static final ColorFilter BLACK_CF = new LightingColorFilter(Color.BLACK, Color.BLACK);

	        public MyQuickAction(Context ctx, int drawableId, int titleId) {
	            super(ctx, buildDrawable(ctx, drawableId), titleId);
	        }
	        
	        private static Drawable buildDrawable(Context ctx, int drawableId) {
	            Drawable d = ctx.getResources().getDrawable(drawableId);
	            d.setColorFilter(BLACK_CF);
	            return d;
	        }
	        
	    }
	}
	
</code>