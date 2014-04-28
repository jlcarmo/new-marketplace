package org.pentaho.marketplace.endpoints;

import org.pentaho.marketplace.endpoints.dtos.IterablePluginOperationResultDTO;
import org.pentaho.marketplace.endpoints.dtos.OperationResultDTO;
import org.pentaho.marketplace.endpoints.dtos.StringOperationResultDTO;
import org.pentaho.marketplace.endpoints.dtos.entities.StatusMessageDTO;
import org.pentaho.marketplace.endpoints.dtos.mappers.interfaces.IPluginDTOMapper;
import org.pentaho.marketplace.domain.model.entities.interfaces.IPlugin;
import org.pentaho.marketplace.domain.model.entities.interfaces.IStatusMessage;
import org.pentaho.marketplace.domain.services.interfaces.IRDO;
import org.pentaho.marketplace.endpoints.dtos.mappers.interfaces.IStatusMessageDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path( "@plugin.java.rest.path.root@" )
public class MarketplaceService {

  private IRDO RDO;
  private IPluginDTOMapper pluginDTOMapper;
  private IStatusMessageDTOMapper statusMessageDTOMapper;

  @Autowired
  public MarketplaceService( IRDO rdo,
                             IPluginDTOMapper pluginDTOMapper,
                             IStatusMessageDTOMapper statusMessageDTOMapper ) {

    //dependency obtained via constructor dependency injection from spring framework
    this.RDO = rdo;
    this.pluginDTOMapper = pluginDTOMapper;
    this.statusMessageDTOMapper = statusMessageDTOMapper;
  }

  @GET
  @Path( "/hello" )
  @Produces( { MediaType.APPLICATION_JSON } )
  public StringOperationResultDTO hello() {

    //create response object
    StringOperationResultDTO result = new StringOperationResultDTO();
    result.result = "Hello World from Marketplace!";

    //status message
    result.statusMessage = new StatusMessageDTO();
    result.statusMessage.code = "OK_CODE";
    result.statusMessage.message = "OK_MESSAGE";

    //return result
    return result;
  }

  @GET
  @Path( "/plugins" )
  @Produces( { MediaType.APPLICATION_JSON } )
  public IterablePluginOperationResultDTO getPlugins() {

    //get plugins from the domain model
    Collection<IPlugin> plugins = this.RDO.getPluginService().getPlugins();

    //transform plugins to DTOs for serialization
    IterablePluginOperationResultDTO result = new IterablePluginOperationResultDTO();
    result.plugins = this.pluginDTOMapper.toDTOs( plugins );

    //status message
    result.statusMessage = new StatusMessageDTO();
    result.statusMessage.code = "OK_CODE";
    result.statusMessage.message = "OK_MESSAGE";

    return result;
  }

  @GET
  @Path( "/plugin/{pluginId}/{versionBranch}" )
  @Produces( { MediaType.APPLICATION_JSON } )
  public OperationResultDTO installPlugin( @PathParam( "pluginId" ) String pluginId,
                                           @PathParam( "versionBranch" ) String versionBranch ) {

    //install plugin
    IStatusMessage statusMessage = this.RDO.getPluginService().installPlugin( pluginId, versionBranch );

    //send installation result
    OperationResultDTO result = new OperationResultDTO();
    result.statusMessage = this.statusMessageDTOMapper.toDTO( statusMessage );
    return result;
  }

  @GET
  @Path( "/plugins/{pluginId}" )
  @Produces( { MediaType.APPLICATION_JSON } )
  public OperationResultDTO uninstallPlugin( @PathParam( "pluginId" ) String pluginId ) {

    //uninstall plugin
    IStatusMessage statusMessage = this.RDO.getPluginService().uninstallPlugin( pluginId );

    //send installation result
    OperationResultDTO result = new OperationResultDTO();
    result.statusMessage = this.statusMessageDTOMapper.toDTO( statusMessage );
    return result;
  }
}
