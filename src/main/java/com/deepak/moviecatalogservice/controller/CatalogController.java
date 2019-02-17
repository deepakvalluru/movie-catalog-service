package com.deepak.moviecatalogservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.moviecatalogservice.model.CatalogItem;
import com.deepak.moviecatalogservice.model.Movie;
import com.deepak.moviecatalogservice.model.Rating;
import com.deepak.moviecatalogservice.service.CatalogService;

@RefreshScope
@RestController
@RequestMapping ( "/catalog" )
public class CatalogController
{
   @Autowired
   private CatalogService catalogService;
   
   @Value("${eureka.instance.instance-id}")
   private String instanceId;
   
   @Value("${message:Movie Catalog service - Config Server is not working..pelase check}")
   private String message;
   
   @RequestMapping( method=RequestMethod.GET, path="/message")
   public String getMessage()
   {
      return this.message + " - with instance id - "+ this.instanceId;
   }

   @RequestMapping ( method = RequestMethod.GET, path = "/{userId}" )
   public List< CatalogItem > getUserCatalog( @PathVariable ( "userId" ) String userId )
   {
      List< Rating > ratingList = catalogService.getRatingList( userId );

      return ratingList.stream().map( rating -> {
         Movie movie = catalogService.getMovie( rating.getMovieId() );
         return new CatalogItem( movie.getName(), movie.getDesc(), rating.getRating() );
      } ).collect( Collectors.toList() );

   }

}
