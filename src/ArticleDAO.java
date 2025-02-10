import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArticleDAO {
	static final String URL = "jdbc:mysql://localhost:3306/projetbdd";
	static final String LOGIN = "root";
	static final String PASS = "";

	public ArticleDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Impossible de charger le pilote MySQL, vérifiez que le fichier .jar est bien importé.");
			e.printStackTrace();
		}
	}

	public int ajouter(Article nouvArticle) {
		Connection con = null;
		PreparedStatement ps = null;
		int retour = 0;

		try {
			con = DriverManager.getConnection(URL, LOGIN, PASS);
			ps = con.prepareStatement("INSERT INTO article (art_designation, art_pu_ht, art_qte_stock) VALUES (?, ?, ?)");
			ps.setString(1, nouvArticle.getDesignation());
			ps.setDouble(2, nouvArticle.getPuHt());
			ps.setInt(3, nouvArticle.getQteStock());
			retour = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}

		return retour;
	}

	public Article getArticle(int reference) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Article retour = null;

		try {
			con = DriverManager.getConnection(URL, LOGIN, PASS);
			ps = con.prepareStatement("SELECT * FROM article WHERE art_reference = ?");
			ps.setInt(1, reference);
			rs = ps.executeQuery();
			if (rs.next()) {
				retour = new Article(rs.getInt("art_reference"), rs.getString("art_designation"), rs.getDouble("art_pu_ht"), rs.getInt("art_qte_stock"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}

		return retour;
	}

	public List<Article> getListeArticles() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Article> retour = new ArrayList<>();

		try {
			con = DriverManager.getConnection(URL, LOGIN, PASS);
			ps = con.prepareStatement("SELECT * FROM article");
			rs = ps.executeQuery();

			while (rs.next()) {
				retour.add(new Article(rs.getInt("art_reference"), rs.getString("art_designation"), rs.getDouble("art_pu_ht"), rs.getInt("art_qte_stock")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
			try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
		}

		return retour;
	}

	public static void main(String[] args) throws SQLException {
		ArticleDAO articleDAO = new ArticleDAO();
		Article a = new Article("Set de 2 raquettes de ping-pong", 149.9, 10);
		int retour = articleDAO.ajouter(a);
		System.out.println(retour + " lignes ajoutées");

		Article a2 = articleDAO.getArticle(1);
		System.out.println(a2);

		List<Article> liste = articleDAO.getListeArticles();
		for (Article art : liste) {
			System.out.println(art.toString());
		}
	}
}