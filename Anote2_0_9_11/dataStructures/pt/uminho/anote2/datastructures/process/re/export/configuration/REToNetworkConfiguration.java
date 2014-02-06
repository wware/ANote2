package pt.uminho.anote2.datastructures.process.re.export.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.uminho.anote2.core.annotation.DirectionallyEnum;
import pt.uminho.anote2.core.annotation.PolarityEnum;
import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.datastructures.database.queries.process.re.info.QueriesREToNetwork;
import pt.uminho.anote2.datastructures.process.re.RelationType;
import pt.uminho.anote2.datastructures.utils.conf.Configuration;
import pt.uminho.anote2.process.IE.re.IRelationsType;
import pt.uminho.anote2.process.IE.re.export.configuration.ICardinality;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfiguration;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationAdvanceOptions;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationAutoOpen;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationEntityOptions;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfigurationRelationOptions;

public class REToNetworkConfiguration implements IREToNetworkConfiguration{

	private IREToNetworkConfigurationEntityOptions entityOptions;
	private IREToNetworkConfigurationRelationOptions relationOptions;
	private IREToNetworkConfigurationAdvanceOptions advanceOptions;
	private IREToNetworkConfigurationAutoOpen autoopenOptions;

	
	
	public REToNetworkConfiguration(
			IREToNetworkConfigurationEntityOptions entityOptions,
			IREToNetworkConfigurationRelationOptions relationOptions,
			IREToNetworkConfigurationAdvanceOptions advanceOptions,
			IREToNetworkConfigurationAutoOpen autoopenOptions) {
		super();
		this.entityOptions = entityOptions;
		this.relationOptions = relationOptions;
		this.advanceOptions = advanceOptions;
		this.autoopenOptions = autoopenOptions;
	}

	public IREToNetworkConfigurationEntityOptions getEntityConfigutrationOptions() {
		return entityOptions;
	}

	public IREToNetworkConfigurationRelationOptions getRelationConfigurationOptions() {
		return relationOptions;
	}

	public IREToNetworkConfigurationAdvanceOptions getAdvanceConfigurations() {
		return advanceOptions;
	}
	
	

	public static Set<DirectionallyEnum> getAllDefaultDirectionally() {
		Set<DirectionallyEnum> result = new HashSet<DirectionallyEnum>();
		result.add(DirectionallyEnum.LeftToRight);
		result.add(DirectionallyEnum.RightToLeft);
		result.add(DirectionallyEnum.Both);
		result.add(DirectionallyEnum.Unknown);
		return result;
	}

	public static Set<PolarityEnum> getAllDefaultPolarities() {
		Set<PolarityEnum> result = new HashSet<PolarityEnum>();
		result.add(PolarityEnum.Positive);
		result.add(PolarityEnum.Negative);
		result.add(PolarityEnum.Conditional);
		result.add(PolarityEnum.Unknown);
		return result;
	}

	public static TreeSet<ICardinality> getAllDefaultCardinalities() {
		TreeSet<ICardinality> cardinalities = new TreeSet<ICardinality>();
		cardinalities.add(new Cardinality(1, 1));
		cardinalities.add(new Cardinality(1, 2));
		cardinalities.add(new Cardinality(2, 1));
		cardinalities.add(new Cardinality(2, 2));
		return cardinalities;
	}

	public static Set<String> getAllLemma() throws SQLException, DatabaseLoadDriverException {
		Set<String> lemmas = new HashSet<String>();
		PreparedStatement getLemmasPS;
			getLemmasPS = Configuration.getDatabase().getConnection().prepareStatement(QueriesREToNetwork.getAllLemmas);
			ResultSet rs = getLemmasPS.executeQuery();
			while(rs.next())
			{
				lemmas.add(rs.getString(1));
			}
			rs.close();
			getLemmasPS.close();
		return lemmas;
	}

	public static SortedSet<IRelationsType> getAllDefaultRelationsType() throws SQLException, DatabaseLoadDriverException {
		SortedSet<IRelationsType> relationTypes = new TreeSet<IRelationsType>();
		Set<Integer> getLeftClassIDs = new HashSet<Integer>();
		Set<Integer> getRightClassIDs = new HashSet<Integer>();
			PreparedStatement getSideClassID = Configuration.getDatabase().getConnection().prepareStatement(QueriesREToNetwork.getAllClassesSide);
			getSideClassID.setString(1, "left");		
			ResultSet rs = getSideClassID.executeQuery();
			while(rs.next())
			{
				getLeftClassIDs.add(rs.getInt(1));
			}
			getSideClassID.setString(1, "right");
			rs = getSideClassID.executeQuery();
			while(rs.next())
			{
				getRightClassIDs.add(rs.getInt(1));
			}
			rs.close();
			getSideClassID.close();
		for(Integer leftClassID:getLeftClassIDs)
		{
			for(Integer rightClassID:getRightClassIDs)
			{
				relationTypes.add(new RelationType(leftClassID, rightClassID));
			}
		}		
		return relationTypes;
	}

	@Override
	public IREToNetworkConfigurationAutoOpen getREToNetworkConfigurationAutoOpen() {
		return autoopenOptions;
	}

}
