package pt.uminho.anote2.process.IE.re;

import java.sql.SQLException;

import pt.uminho.anote2.core.database.exception.DatabaseLoadDriverException;
import pt.uminho.anote2.process.IE.IRESchema;
import pt.uminho.anote2.process.IE.network.IEdge;
import pt.uminho.anote2.process.IE.network.INetwork;
import pt.uminho.anote2.process.IE.network.INode;
import pt.uminho.anote2.process.IE.re.export.configuration.IREToNetworkConfiguration;

/**
 * Interface to transform a RE Schema into a {@link INetwork}
 * 
 * @author Hugo Costa
 *
 */
public interface IRESchemaToNetwork {
	public INetwork<INode,IEdge> getNetwork(IRESchema rePorcess,IREToNetworkConfiguration configurations) throws SQLException, DatabaseLoadDriverException;
	public void setcancel();
}
