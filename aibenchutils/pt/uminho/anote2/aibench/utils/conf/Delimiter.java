/*
 * Copyright 2010
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 * 
 * Created inside the SysBioPseg Research Group (http://sysbio.di.uminho.pt)
 */
package pt.uminho.anote2.aibench.utils.conf;

/**
 * 
 * @author pmaia
 *
 */
public enum Delimiter {
	
	
	COMMA{
		public String toString() {
			return ",";
		}
		
		public String getName() {
			return "Comma ( , )";
		}
	},
	TAB{
		public String toString() {
			return "\t";
		}
		
		public String getName() {
			return "Tab ( \t )";
		}
	},
	WHITE_SPACE{
		public String toString() {
			return " ";
		}
		
		public String getName() {
			return "White Space ( )";
		}
	},
	SEMICOLON{
		public String toString() {
			return ";";
		}
		
		public String getName() {
			return "Semi-colon ( ; )";
		}
	},
	USER{					
		public String toString(){
			return userDelimiter;
		}
	};

	public void setUserDelimiter(String delimiter) {
		this.userDelimiter = delimiter;
	}
	
	public String getName(){
		return this.toString();
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	protected String userDelimiter = null;
	protected String name;
	
	

}
