/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fantItem;

import com.mycompany.mavenproject1.domain.Item;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import no.ntnu.tollefsen.auth.AuthenticationService;
import no.ntnu.tollefsen.auth.Group;
import no.ntnu.tollefsen.auth.User;



/**
 *
 * @author Bruker
 */

@Path("service")
@Stateless
@DeclareRoles({Group.USER})
public class FantService {
    
    
    @Inject
    AuthenticationService authenticationService;
    
    @PersistenceContext
    EntityManager em;
    
    @Context
    SecurityContext securityContext;
    
   @GET
    @Path("items")
    @RolesAllowed({Group.USER})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> getItems() {
        return em.createNamedQuery(Item.FIND_ALL_ITEMS, Item.class).getResultList();
    }
    
    @POST 
    @Path("addItem")
    @RolesAllowed({Group.USER})
    public Response addItem(
            @FormParam("title")String title,
            @FormParam("description")String description,
            @FormParam("price") int price
                           )
            
    {
        User user = this.getCurrentUser();
        Item newItem = new Item();
        
        newItem.setItemOwner(user);
        newItem.setTitle(title);
        newItem.setDescription(description);
        newItem.setPrice(price);
        
         em.persist(newItem);

        return Response.ok().build();
    }
            

    private User getCurrentUser(){
        //System.out.printf("Pname low <%s>", principal.getName());
        return em.find(User.class, securityContext.getUserPrincipal().getName());
    }
}
