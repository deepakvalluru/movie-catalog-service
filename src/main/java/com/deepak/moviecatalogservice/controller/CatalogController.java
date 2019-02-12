package com.deepak.moviecatalogservice.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.deepak.moviecatalogservice.model.CatalogItem;
import com.deepak.moviecatalogservice.model.Movie;
import com.deepak.moviecatalogservice.model.Rating;

@RestController
@RequestMapping ( "/catalog" )
public class CatalogController
{

   @Autowired
   private RestTemplate restTemplate;

   @RequestMapping ( method = RequestMethod.GET, path = "/{userId}" )
   public List< CatalogItem > getUserCatalog( @PathVariable ( "userId" ) String userId )
   {
      ResponseEntity< List< Rating > > ratingList =
                                                  this.restTemplate.exchange( "http://localhost:8083/ratings/users/"
                                                                              + userId,
                                                                              HttpMethod.GET,
                                                                              null,
                                                                              new ParameterizedTypeReference< List< Rating > >()
                                                                              {
                                                                              } );
      
      
      return ratingList.getBody().stream().map( rating -> {
         Movie movie = this.restTemplate.getForEntity( "http://localhost:8082/movies/" + rating.getMovieId(),
                                                       Movie.class )
                                        .getBody();
         return new CatalogItem( movie.getName(), movie.getDesc(), rating.getRating() );
      }
      ).collect( Collectors.toList() );
      
   }

   private List< CatalogItem > getCatalogItems()
   {
      return Stream.< CatalogItem > builder()
                   .add( new CatalogItem( "Hello", "Hello-Desc", 5 ) )
                   .build()
                   .collect( Collectors.toList() );
   }
}
