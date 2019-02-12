package com.deepak.moviecatalogservice.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.deepak.moviecatalogservice.model.CatalogItem;

@RestController
@RequestMapping("/catalog")
public class CatalogController
{
   @RequestMapping(method=RequestMethod.GET, path = "/{userId}")
   public List<CatalogItem> getUserCatalog( @PathVariable("userId") String userId )
   {
      return getCatalogItems();
   }
   
   private List<CatalogItem> getCatalogItems()
   {
      return Stream.< CatalogItem > builder()
                   .add( new CatalogItem( "Hello", "Hello-Desc", 5 ) )
                   .build()
                   .collect( Collectors.toList() );
   }
}
