package pt.uminho.anote2.nergate.abner;


public enum ABNERTrainingModel {
	BIOCREATIVE{
		public String toValue(){
			return "BIOCREATIVE";
		}		
	},
	NLPBA{
		public String toValue(){
			return "NLPBA";
		}		
	};
	
	public String toValue() {
		return this.toValue();
	}
}
