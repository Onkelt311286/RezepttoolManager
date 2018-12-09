package de.tkoehler.rezepttool.manager.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ImporterServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ImporterServiceTest {
	
	@InjectMocks
	private ImporterServiceImpl objectUnderTest;

	@Test(expected = ImporterServiceException.class)
	public void importRecipe_NullParamter_throwsImportierenServiceException() throws Exception {
		objectUnderTest.importRecipe(null);
	}
	
	@Test(expected = ImporterServiceException.class)
	public void importRecipe_WrongParameter_throwsImportierenServiceException() throws Exception {
		objectUnderTest.importRecipe("fdsanjkfdsa");
	}
	
	@Test(expected = ImporterServiceException.class)
	public void extractRecipeJSonFromURL_NullParamter_throwsImportierenServiceException() throws Exception {
		objectUnderTest.extractRecipeJSonFromURL(null);
	}
	
	@Test(expected = ImporterServiceException.class)
	public void extractRecipeJSonFromURL_EmptyWebSite_throwsImportierenServiceException() throws Exception {
		objectUnderTest.extractRecipeJSonFromURL(new Document(""));
	}
	
	@Test(expected = ImporterServiceException.class)
	public void extractRecipeJSonFromURL_NoJSonDataInWebSite_throwsImportierenServiceException() throws Exception {
		objectUnderTest.extractRecipeJSonFromURL(new Document("<!DOCTYPE html><html><head></head><body><script></script></body></html>"));
	}
	
	@Test
	public void extractRecipeJSonFromURL_CorrectData_success() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		String data = objectUnderTest.extractRecipeJSonFromURL(Jsoup.connect(url).get());
		assertThat(data, containsString("Antipasti - marinierte Champignons"));
	}
}
