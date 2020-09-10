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
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    
    @Inject
    Mail mailService;
    
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
    
    @DELETE
    @Path("delete")
    @RolesAllowed({Group.USER})
    public Response delete(@QueryParam("itemid") Long itemid) {
        Item item = em.find(Item.class, itemid);
        if (item != null){
            User user = this.getCurrentUser();
            if (item.getItemBuyer().getUserid().equals(user.getUserid())){
                em.remove(item);
                return Response.ok().build();
            }
        }
        
        return Response.notModified().build();
    }
 
    @PUT
    @Path("email")
    @RolesAllowed({Group.USER})
    public Response setEmail(
            @QueryParam("uid") String uid,
            @FormParam("email") String email) {
        User user = this.getCurrentUser();
        if (user.getEmail() == null) {
            user.setEmail(email);
        }
        return Response.ok().build();
    }
    
    @PUT
    @Path ("purchase")
    @RolesAllowed({Group.USER})
    public Response purchase(@QueryParam("itemid") Long itemid)
    {
        Item item = em.find(Item.class, itemid);
        if (item != null){
           User itemBuyer = this.getCurrentUser();
           item.setItemBuyer(itemBuyer);
           mailService.sendEmail(item.getItemOwner().getEmail(), "Item sold", "Your item listed in the fant service has now been sold to the user" + itemBuyer);
           return Response.ok().build();
        }
        return Response.notModified().build();
    }
    

    private User getCurrentUser(){
        //System.out.printf("Pname low <%s>", principal.getName());
        return em.find(User.class, securityContext.getUserPrincipal().getName());
    }
}
