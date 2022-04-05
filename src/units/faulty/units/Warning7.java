package faulty.units;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import org.jlayer.annotations.*;

@LayerUnit 
class Warning7 {
	
	Warning7(int par){
		noItem = par;
	}
	
	int noItem;
	
	@LayerField
	int intItem;
	
	@LayerMethod
	void methodItem(){
		intItem = noItem;
	}
	
	@LayerMethod 											// warning:
	public void throwsMethod() throws IOException {			// a layer method must not have a throws clause
		
		FileReader fr = new FileReader("myfile.txt");
		BufferedReader textReader = new BufferedReader(fr);
		
		int numberOfLines = 3;
		String[] textData = new String[numberOfLines];
		for (int i = 0; i < numberOfLines; i++) {
			textData[i] = textReader.readLine();
		}
		
		textReader.close();

	}

}
