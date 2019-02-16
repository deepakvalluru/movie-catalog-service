package com.deepak.moviecatalogservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.moviecatalogservice.model.CatalogItem;
import com.deepak.moviecatalogservice.model.Movie;
import com.deepak.moviecatalogservice.model.Rating;
import com.deepak.moviecatalogservice.service.CatalogService;

@RestController
@RequestMapping ( "/catalog" )
public class CatalogController
{
   @Autowired
   private CatalogService catalogService;

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
