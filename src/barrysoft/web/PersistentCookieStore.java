package barrysoft.web;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

import barrysoft.logs.Logger;

public class PersistentCookieStore 
implements CookieStore, Runnable
{
 CookieStore store;

 public PersistentCookieStore() {
     // get the default in memory cookie store
     store = new CookieManager().getCookieStore();

     // todo: read in cookies from persistant storage
     // and add them store

     // add a shutdown hook to write out the in memory cookies
     Runtime.getRuntime().addShutdownHook(new Thread(this)); 
 }

 public void run() {
     // todo: write cookies in store to persistent storage
 }

 public void	add(URI uri, HttpCookie cookie) {
	 if (cookie == null) return;
 	 Logger.debug("New cookie ("+cookie.getDomain()+"): "+cookie.getName(),Logger.HIGHEST_VERBOSITY);
 	 Logger.debug("Cookies in storage: "+store.getCookies().toString(), Logger.HIGHEST_VERBOSITY);
     store.add(uri, cookie);
 }

 public List<HttpCookie> get(URI uri) {
	 Logger.debug("Getting cookie for: "+uri.toASCIIString()+" = "+store.get(uri).toString(), Logger.HIGHEST_VERBOSITY);
	 Logger.debug("Cookies in storage: "+store.getCookies().toString(), Logger.HIGHEST_VERBOSITY);
	 return store.get(uri);
 }

 public List<HttpCookie> getCookies() {
     return store.getCookies();
 }
 
 public List<URI> getURIs() {
     return store.getURIs();
 }

 public boolean remove(URI uri, HttpCookie cookie) {
     return store.remove(uri, cookie);
 }

 public boolean removeAll()  {
     return store.removeAll();
 }
}