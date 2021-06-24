package load;


import audio.AudioMaster;
import audio.Source;
import fontMeshCreator.FontType;
import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import renderEngine.GuiRenderer;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import textures.ModelTexture;

public class LoadMaster {
	
	public static Loader loader = new Loader();
	//load shader
	public static GuiRenderer guiRenderer;
	public static MasterRenderer renderer;
	//model
	public static ModelData player_data;
	public static RawModel player_model;
	public static TexturedModel player_TexturedModel;
	
	public static ModelData enemy_data;
	public static RawModel enemy_model;
	public static TexturedModel enemy1_TexturedModel;
	public static TexturedModel enemy2_TexturedModel;
	
	public static ModelData bullet_data;
	public static RawModel bullet_model;
	public static TexturedModel bullet_TexturedModel;
	
	public static ModelData plasmaBall_data;
	public static RawModel plasmaBall_model;
	public static TexturedModel plasmaBall_TexturedModel;
	
	public static TexturedModel plasmaBallGreen_TexturedModel;
	
	public static ModelData asteroid1_data;
	public static RawModel asteroid1_model;
	public static TexturedModel asteroid1_TexturedModel;
	
	public static ModelData missile_data;
	public static RawModel missile_model;
	public static TexturedModel missile_TexturedModel;
	
	public static Source source;
	
	//GUI
	
	//Fonts
	public static FontType FONT_Airstrike;
	
	//Sound Effects
	public static int laser_SE;
	public static int explode_SE;
	
	public static int click_SE;
	public static int bgMusic1;
	public static int missileDrop_SE;
	public static int missileFiring_SE;

	
	
	
	public static void load() {
		renderer = new MasterRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		source = new Source(0.1f);
		player_data = OBJFileLoader.loadOBJ("SpaceShip");
		player_model = loader.loadToVAO(player_data.getVertices(), player_data.getTextureCoords(), player_data.getNormals(), player_data.getIndices());
		player_TexturedModel = new TexturedModel(player_model,new ModelTexture(loader.loadTexture("SpaceShip")));
		
		enemy_data = OBJFileLoader.loadOBJ("ship6");
		enemy_model = loader.loadToVAO(enemy_data.getVertices(), enemy_data.getTextureCoords(), enemy_data.getNormals(), enemy_data.getIndices());
		enemy1_TexturedModel = new TexturedModel(enemy_model,new ModelTexture(loader.loadTexture("Enemy2")));
		enemy_data = OBJFileLoader.loadOBJ("ship9");
		enemy_model = loader.loadToVAO(enemy_data.getVertices(), enemy_data.getTextureCoords(), enemy_data.getNormals(), enemy_data.getIndices());
		enemy2_TexturedModel = new TexturedModel(enemy_model,new ModelTexture(loader.loadTexture("ship9")));
		
		bullet_data = OBJFileLoader.loadOBJ("Bullet");
		bullet_model = loader.loadToVAO(bullet_data.getVertices(), bullet_data.getTextureCoords(), bullet_data.getNormals(), bullet_data.getIndices());
		bullet_TexturedModel = new TexturedModel(bullet_model,new ModelTexture(loader.loadTexture("Bullet")));
		
		plasmaBall_data = OBJFileLoader.loadOBJ("PlasmaBall");
		plasmaBall_model = loader.loadToVAO(plasmaBall_data.getVertices(), plasmaBall_data.getTextureCoords(), plasmaBall_data.getNormals(), plasmaBall_data.getIndices());
		plasmaBall_TexturedModel = new TexturedModel(plasmaBall_model,new ModelTexture(loader.loadTexture("PlasmaBall")));
		plasmaBallGreen_TexturedModel =  new TexturedModel(plasmaBall_model,new ModelTexture(loader.loadTexture("PlasmaBallGreen")));
		
		missile_data = OBJFileLoader.loadOBJ("missile");
		missile_model = loader.loadToVAO(missile_data.getVertices(), missile_data.getTextureCoords(), missile_data.getNormals(), missile_data.getIndices());
		missile_TexturedModel = new TexturedModel(missile_model,new ModelTexture(loader.loadTexture("Red")));
		
		asteroid1_data = OBJFileLoader.loadOBJ("asteroid1");
		asteroid1_model = loader.loadToVAO(asteroid1_data.getVertices(), asteroid1_data.getTextureCoords(), asteroid1_data.getNormals(), asteroid1_data.getIndices());
		asteroid1_TexturedModel = new TexturedModel(asteroid1_model,new ModelTexture(loader.loadTexture("asteroid1")));
		

		laser_SE = AudioMaster.loadSound("/audios/laser.wav");
		explode_SE = AudioMaster.loadSound("/audios/explosion.wav");
		missileDrop_SE = AudioMaster.loadSound("/audios/missileDrop.wav");
		missileFiring_SE = AudioMaster.loadSound("/audios/missileFiring.wav");
		
		click_SE = AudioMaster.loadSound("/audios/click.wav");
		bgMusic1 = AudioMaster.loadSound("/audios/Ascent_to_the_Station.wav");
		
		
//		FONT_arial = new FontType(LoadMaster.loader.loadFontTexture("arial"), "/fonts/arial.fnt");
//		FONT_calibri = new FontType(LoadMaster.loader.loadFontTexture("calibri"), "/fonts/calibri.fnt");
//		FONT_harrington = new FontType(LoadMaster.loader.loadFontTexture("harrington"), "/fonts/harrington.fnt");
//		FONT_Airstrike = new FontType(LoadMaster.loader.loadFontTexture("arial"), "/fonts/Airstrike.fnt");
		FONT_Airstrike = new FontType(LoadMaster.loader.loadFontTexture("Airstrike"), "/fonts/Airstrike.fnt");
		

	}

}
