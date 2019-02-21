package com.deepak.moviecatalogservice.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.deepak.moviecatalogservice.model.Movie;
import com.deepak.moviecatalogservice.model.Rating;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class CatalogService
{
   private static final Logger logger = LoggerFactory.getLogger( CatalogService.class );
   
   @Autowired
   private RestTemplate restTemplate;

   @HystrixCommand ( fallbackMethod = "getRatingsForUserFallback" )
   public List< Rating > getRatingList( String userId )
   {
      logger.debug( "Getting ratings list" );
      ResponseEntity< List< Rating > > ratingList =
                                                  this.restTemplate.exchange( "http://ratings-data-service/ratings/users/"
                                                                              + userId,
                                                                              HttpMethod.GET,
                                                                              null,
                                                                              new ParameterizedTypeReference< List< Rating > >()
                                                                              {
                                                                              } );
      if( ratingList!= null )
      {
         logger.debug( "Ratings List - {}", ratingList.getBody() );
         return ratingList.getBody();
      }
      else
      {
         logger.error( "No ratings list for the user - {}", userId );
         return null;
      }

   }

   public Movie getMovie( String movieId )
   {
      ResponseEntity< Movie > movie = this.restTemplate.getForEntity( "http://movie-info-service/movies/" + movieId,
                                                                      Movie.class );
      if( movie!= null )
      {
         logger.debug( "Movie Details - {}", movie.getBody() );
         return movie.getBody();
      }
      else
      {
         logger.error( "No movie returned for the movieId - {}", movieId );
         return null;
      }
   }

   public List< Rating > getRatingsForUserFallback( String userId )
   {
      logger.warn( "Going to fallback conent for getting ratings list" );
      return Stream.< Rating > builder()
                   .add( new Rating( "1234", 4 ) )
                   .add( new Rating( "1236", 6 ) )
                   .build()
                   .collect( Collectors.toList() );
   }

}
