/**
 *	Utilities for handling HTML
 *
 *	@author	
 *	@since	
 */
public class HTMLUtilities {
	// NONE = not nested in a block, COMMENT = inside a comment block
	// PREFORMAT = inside a pre-format block
	private enum TokenState { NONE, COMMENT, PREFORMAT };
	// the current tokenizer state
	private TokenState state; 

	public HTMLUtilities()
	{
		state = state.NONE;
	}
	/**
	 *	Break the HTML string into tokens. The array returned is
	 *	exactly the size of the number of tokens in the HTML string.
	 *	Example:	HTML string = "Goodnight moon goodnight stars"
	 *				returns { "Goodnight", "moon", "goodnight", "stars" }
	 *	@param str			the HTML string
	 *	@return				the String array of tokens
	 */
	public String[] tokenizeHTMLString(String str) 
	{
		// make the size of the array large to start
		String[] result = new String[10000];
		int counter = 0;

		for(int i = 0; i < str.length(); i++)
		{	
			char letter = str.charAt(i);
			char letterBefore = '\0';
			char letterAfter = '\0';

			if(!Character.isWhitespace(letter))
			{
				if(i != 0)
					letterBefore = str.charAt(i - 1);
				if(i != str.length() - 1)
					letterAfter = str.charAt(i + 1);
				int asciiLetter = (int)(str.charAt(i));

				switch(state)
				{
					case NONE:
						if(letter == '<' && letterAfter == '!' && str.charAt(i + 2) == '-' && str.charAt(i + 3) == '-')
						{
							state = state.COMMENT;
						}
						else if(letter == '<' && letterAfter == 'p' && str.charAt(i + 2) == 'r' && str.charAt(i + 3) == 'e' && str.charAt(i + 4) == '>')
						{
							state = state.PREFORMAT;
						} 
						else
						{
							if(letter == '<')
							{
								int recentIndex = i;
								int endIndex = str.indexOf(">", i);

								result[i] = str.substring(recentIndex, endIndex + 1);
								i += endIndex - recentIndex;
								counter++;
							}
							else if(letter == '-' && Character.isWhitespace(letterAfter) && Character.isDigit(str.charAt(i + 2)))
							{
								boolean found = true;
								int recentIndex = i;
								int endIndex = -1;
								String digit = "";
								int j;
								for(j = i + 2; j < str.length() && found; j++)
								{
									if (!Character.isDigit(str.charAt(j)) && !(str.charAt(j) == '-') && !(str.charAt(j) == 'e') && !(str.charAt(j) == '.')) 
									{		
										endIndex = j;
										found = false;
									}
								}
								if(endIndex == -1)
									endIndex =  j;
								result[i] = "-";
								result[i + 1] = str.substring(recentIndex, endIndex);
								i += endIndex - recentIndex + 2;
								counter+=2;
							}
							else if(Character.isDigit(letter) || letter == '-' && Character.isDigit(letterAfter))
							{
								boolean found = true;
								int recentIndex = i;
								int endIndex = -1;
								String digit = "";
								int j;
								for(j = i; j < str.length() && found; j++)
								{
									if (!Character.isDigit(str.charAt(j)) && !(str.charAt(j) == '-') && !(str.charAt(j) == 'e') && !(str.charAt(j) == '.')) 
									{		
										endIndex = j;
										found = false;
									}
								}
								if(endIndex == -1)
								{
									endIndex =  j;
								}
								digit = str.substring(recentIndex, endIndex);
								result[i] = str.substring(recentIndex, endIndex);
								i += endIndex - recentIndex - 1;
								counter++;

							}

							else if(Character.isLetter(letter))
							{
								int recentIndex = i;
								int endIndex = -1;
								boolean found = true;
								int j;
								for(j = i; j < str.length() && found; j++)
								{
									if (!Character.isLetter(str.charAt(j)) && !(str.charAt(j) == '-')) 
									{		
										endIndex = j;
										found = false;
									}
								}
								if(endIndex == -1)
									endIndex = j;

								result[i] = str.substring(recentIndex, endIndex);
								i += endIndex - recentIndex - 1;
								counter++;


							}

							else if(isPunctuation(letter))
							{
								result[i] = letter + "";
								counter++;
							}
							
							
						}
						break;
						
				case COMMENT:
					if(letter == '-' && letterAfter == '-' && str.charAt(i + 2) == '>')
					{
						state = state.NONE;
						i += 2;
					}
					break;
				case PREFORMAT:
					//System.out.println("in prefomrat");
					if(letter == '<' && letterAfter == '/' &&  str.charAt(i + 2) == 'p' && str.charAt(i + 3) == 'r' && str.charAt(i + 4) == 'e' && str.charAt(i + 5) == '>')
					{
						state = state.NONE;
						
						i += 3;
					}
					else
					{
					int endIndex = str.indexOf("</pre>", i);
					
	
					result[i] = str.substring(i, endIndex);
					i = endIndex - i -1;
						counter++;

					} 	
					break;
					
					
				}			

			}
		}

		String[] ans = new String[counter];
		int index = 0;
		for(int i = 0; i < result.length; i++)
		{
			if(result[i] != null)
			{
				ans[index] = result[i];
				index++;
			}
		}
		// return the correctly sized array
		return ans;
	}

	public boolean isPunctuation(char letterIn)
	{
		if(Character.isLetterOrDigit(letterIn))
			return false;
		else
			return true;
	}
	/**
	 *	Print the tokens in the array to the screen
	 *	Precondition: All elements in the array are valid String objects.
	 *				(no nulls)
	 *	@param tokens		an array of String tokens
	 */
	public void printTokens(String[] tokens) {
		if (tokens == null) return;
		for (int a = 0; a < tokens.length; a++) {

			if (a % 5 == 0) 
				System.out.print("\n  ");
			System.out.print("[token " + a + "]: " + tokens[a] + " ");
		}
		System.out.println();
	}

}
