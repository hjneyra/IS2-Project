package crawler;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OechsleCrawler extends Crawler {

  public OechsleCrawler() {
    url = "http://www.oechsle.pe";
    categories = new Vector<>();

  }

  @Override
  protected List<Category> buildCategories(Document homePage) {

    return this.categories;
  }

  @Override
  public List<Category> getCategories() {
    return categories;
  }

  @Override
  public List<SubCategory> getSubCategories() {
    return null;
  }

  @Override
  public List<SubSubCategory> getSubSubCategories() {
    return null;
  }


  private void crawlingShop() {
    try {
      Document homePage = getHtmlFromUrl(this.url);
      categories = crawlingCategories(homePage);
      crawlingSubCategories(homePage);
      crawlingSubSubCategories(homePage);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<Category> crawlingCategories(Document doc) {
    Vector<Category> categoriesAux = new Vector<>();
    Elements cats = doc.getElementsByAttributeValue("class", "wrap-hover");
    for (Element catElement : cats) {
      Category category = new Category();
      String nameCategory = catElement.text();

      Element aux = catElement.getElementsByTag("a").first();
      String urlCategory = this.url + aux.attr("href");

      category.setName(nameCategory);
      category.setUrl(urlCategory);
      categoriesAux.add(category);
    }
    return categoriesAux.subList(0, categoriesAux.size() - 2);
  }

  private List<SubCategory> crawlingSubCategories(Document doc) {

    Elements subCats = doc.getElementsByAttributeValue("class", "menu-preview wrap-hover");
    int cont = 0;

    for (Element catElementItems : subCats) {
      Vector<SubCategory> subCategoriesAux = new Vector<>();
      String beforeSubCat = "";
      String nameSubCategory = "";
      String urlSubCategory = "";
      Elements catsElements = catElementItems.select("div[class^=item]");
      for (Element catItem : catsElements) {
        SubCategory subCategory = new SubCategory();

        Elements nameSubCats = catItem.getElementsByAttributeValue("class", "tit");

        nameSubCategory = nameSubCats.text();
        urlSubCategory = this.url + nameSubCats.select("a").first().attr("href");

        subCategory.setName(nameSubCategory);
        subCategory.setUrl(urlSubCategory);

        subCategoriesAux.add(subCategory);
      }
      categories.get(cont).setSubCategories(subCategoriesAux);
      cont++;
    }

    return null;
  }

  private void crawlingSubSubCategories(Document doc) {
    Elements cats = doc.getElementsByAttributeValue("class", "menu-preview wrap-hover");
    int contC = 0;

    for (Element catItem : cats) {
      int contS = 0;
      Elements catsElements = catItem.getElementsByAttributeValue("class", "info"); // categories

      for (Element catElement : catsElements) {
        Vector<SubSubCategory> subSubCatAux = new Vector<>();
        Element subCats = catElement.getElementsByAttributeValue("class", "tit")
            .first(); // subcategories
        Elements subSubCats = catElement.select("a[class^=sub]"); // subsubcategories
        for (Element subSubCatElement : subSubCats) {
          SubSubCategory subSubCategory = new SubSubCategory();
          String nameSubSubCat = subSubCatElement.text();
          String urlSubSubCat = this.url + subSubCatElement.attr("href");

          subSubCategory.setName(nameSubSubCat);
          subSubCategory.setUrl(urlSubSubCat);

          subSubCatAux.add(subSubCategory);
        }
        categories.get(contC).getSubCategories().get(contS).setSubSubCategories(subSubCatAux);
        contS++;
      }
      contC++;
    }
  }

  private Document getHtmlFromUrl(String url) throws IOException, HttpStatusException {
    return Jsoup.connect(url).get();
  }

}
