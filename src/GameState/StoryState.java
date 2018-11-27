package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import TileMap.Background;

public class StoryState extends GameState {

	private Background bg;

	private Color titleColor;
	private Font font;
	
	private int x;
	private int speed;
	private int acceleration;

	private String[] story = {
			"The year 784...\r\n",
			"One blistering hot night, a miner decided to dig upward",
			"towards the roof of the tunnel, to see if they really ",
			"trapped in this hell. \"There is a limit to dig ",
			"downwards, well, how about digging upwards?\" That ",
			"thought had come to him suddenly a few days ago, as he",
			"dug with his shovel, deep in a coal mine. In a way, you",
			"could call it divine inspiration. And so, for the next",
			"few days, he wandered along the tunnel, finally settling",
			"on a spot where he found a big abandoned scrap heap. ",
			"No one would chance upon him, and the heavy darkness ",
			"would shelter him and his task from the prying eyes of ",
			"the underworld guards below.\r\n","",
			"On the next night, he began. Using his trusty shovel, ",
			"the miner began to dig in the form of a spiral staircase. ",
			"The work went smoothly, and before long, the pit was as ",
			"deep as he was tall. When he could no longer shed dirt ",
			"outside it, he stuffed it into sacks, climbing down the ",
			"ladder to discard it. Completely dedicated to the task ",
			"at hand, he took only a few short breaks to drink water ",
			"and to ease his weary muscles.\r\n","",
			"After spending twenty years of his life digging without ",
			"a day of rest, the miner knew he was an expert at it. ",
			"And during that time, he'd learned to dig deeper, more ",
			"quickly, and more efficiently than anyone else. Tunnels ",
			"that would stump any other miner were no match for his ",
			"shovel. But that day was different. He dug for hours, ",
			"yet never seemed to get anywhere. As he dug, he probed ",
			"to either side of the main pit with his shovel, but it ",
			"was of no use. The roof of the tunnel seemed to go up ",
			"forever. Still, he persevered.\r\n","",
			"Any thought of digging his way to an easy life outside ",
			"of tunnel had been long forgotten. He simply wanted to ",
			"conquer the tunnel. \"I'm willing to bet all the years ",
			"I've spent digging that I will beat this tunnel,\" he ",
			"said to himself, wiping the sweat from his brow. The ",
			"hole was more than four or five times the miner's height ",
			"when he struck a hard rock. He wondered, \"Bedrock?\" ",
			"But it appeared to be of the same material as the wall ",
			"at the deep bottom of the tunnel. The miner swung his ",
			"shovel down hard on the rock. The shovel shattered, but ",
			"the rock was unscathed. The miner sighed, and his sigh ",
			"was deeper than any hole he'd dug in the previous twenty ",
			"years.\r\n","",
			"With a frown, his friend questioned, \"The wall?\" ",
			"\"It's weird, isn't?\" the miner said, lifting his ",
			"drink to his lips. \"Who knew that there will be a wall",
			"on the roof of the tunnel too?\" The pair sat across from ",
			"one another at a table in the back if a tavern in a seedy ",
			"part of town. Most days, the miner would go there with his ",
			"friend, his only friend, after work.\r\n","",
			"That day, the day after he'd tried to dig upward from the",
			"tunnel, they'd gone there as usual, once the workday was ",
			"done. And then, the miner told his friend the story, certain",
			"that his friend could keep a secret. \"Maybe we're surrounded ",
			"by walls not only below the ground but above the ground, too.",
			"\" said the miner. \"Tell me something... What, exactly, are ",
			"the walls?\" His friend cut him off with a cough, glancing ",
			"around the tavern. The patrons were all busy drinking, ",
			"hitting on the tavern maid, or yelling at one another. No",
			"one was watching them. But even so, the miner decided not to",
			"say another word about it. If anyone heard him, the Military ",
			"Police would be there in an instant. \"Does it really matter?\"",
			"his friend asked. \"Just keep on living here as you always have.",
			"You're poor, but you have a job every day and enough money that",
			"you can drink. Inst that enough?\" \"Yeah,\" the miner replied. ",
			"\"Yeah, it is. I'll go back to digging. In the end, I guess ",
			"that's my lot in life.\"\r\n","",
			"But the next day, the miner didn't show up for work. ",
			"Not the next... Nor the day after that. His friend went ",
			"to his house many times, but never saw any sign of him. the",
			"miner had no relatives, nor other close friends, so there was ",
			"no one to ask where he might have gone. Unsure what to do, his ",
			"friend eventually went to the Garrison and told them the whole ",
			"story. The next day, the Military Police and the Garrison ",
			"organized a large-scale search party. It was a little excessive",
			"for one poor miner, even if that miner was a criminal who'd tried",
			"to pass above the tunnel. His friend couldn't understand why ",
			"they were so bent on finding him. But they never found a trace ",
			"of the miner, nor even the pit he'd dug. And one day, his friend",
			"also vanished, never to be seen again.\r\n","",
	};
	
	public StoryState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		bg = new Background("/Backgrounds/menubg.gif", 1);
		bg.setVector(-0.1, 0);
		
		x = 0;
		speed = 0;
		acceleration = 2;

		titleColor = new Color(128, 0, 0);
		font = new Font("Arial", Font.PLAIN, 12);
	}

	@Override
	public void update() {
		bg.update();
		if(x<=-1550) {
			skip();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// draw bg
		bg.draw(g);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(font);
		
		for (int i = 0; i < story.length; i++) {
			g.drawString(story[i], 5, 140 + i * 15 + x +100);
		}
		
		speed+=acceleration;
		if(speed>=10) {
			x--;
			speed=0;
		}
	}

	@Override
	public void keyPressed(int k) {
		//skip();
		if(k == KeyEvent.VK_W || k == KeyEvent.VK_UP) acceleration = 1;
		else if( k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN) acceleration = 300;
		if (k == KeyEvent.VK_SPACE	|| k == KeyEvent.VK_ENTER 
		 || k == KeyEvent.VK_LEFT	|| k == KeyEvent.VK_RIGHT
		 || k == KeyEvent.VK_A		|| k == KeyEvent.VK_D)
			skip();
	}

	private void skip() {
		gsm.setState(GameStateManager.TOWN1STATE);
	}

	@Override
	public void keyReleased(int k) {
		acceleration = 2;
	}

}
