package Asgardia.Config;

import java.util.Properties;
import java.io.*;

public class ConfigurationLoader
{
	private static ConfigurationLoader instance;
	
	public static ConfigurationLoader getInstance () 
	{
		if (instance == null) {
			instance = new ConfigurationLoader () ;
		}
		return instance;
	}
	
	public ConfigurationLoader () 
	{
		System.out.printf ("Load server config...") ;
		Properties p = new Properties () ;
		
		try {
			p.load (new FileInputStream ("configs/asgardia.properties") ) ;
			Configurations.RateExp = Integer.valueOf (p.getProperty ("RATE_EXP") ) ;
			Configurations.RatePetExp = Integer.valueOf (p.getProperty ("RATE_PET_EXP") ) ;
			Configurations.RateDropItem = Integer.valueOf (p.getProperty ("RATE_DROP_ITEM") ) ;
			Configurations.RateDropGold = Integer.valueOf (p.getProperty ("RATE_DROP_GOLD") ) ;
			Configurations.RateLawful = Integer.valueOf (p.getProperty ("RATE_LAWFUL") ) ;
			Configurations.RateMaxWeight = Integer.valueOf (p.getProperty ("RATE_MAX_WEIGHT") ) ;
			Configurations.RateMaxPetWeight = Integer.valueOf (p.getProperty ("RATE_PET_MAX_WEIGHT") ) ;
			
			p.load (new java.io.FileInputStream ("configs/server.properties") ) ;
			Configurations.USE_GUI = Boolean.valueOf (p.getProperty ("UseLocalGui") ) ;
			Configurations.SIGHT_RAGNE = Integer.valueOf (p.getProperty ("SightRange") ) ;
			Configurations.CASINO = Boolean.valueOf (p.getProperty ("CasinoSystem") ) ;
			Configurations.MONSTER_GENERATOR_UPDATE_RATE = Integer.valueOf (p.getProperty ("MonsterGeneratorUpdateRate") ) ;
			Configurations.MONSTER_AI_UPDATE_RATE = Integer.valueOf (p.getProperty ("MonsterAiUpdateRate") ) ;
			Configurations.DEFAULT_MOVEMENT_RANGE = Integer.valueOf (p.getProperty ("DefaultMovementRange") ) ;			
			System.out.printf ("success\n") ;
			
		} catch (Exception e) {
			System.out.println (e.toString () ) ;
		}
		
	}
	
	public void Update () 
	{
		//
	}
}
