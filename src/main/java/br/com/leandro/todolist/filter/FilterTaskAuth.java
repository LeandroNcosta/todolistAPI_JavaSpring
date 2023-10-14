package br.com.leandro.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.leandro.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// public class FilterTaskAuth implements Filter {
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    // executar alguma acao
    // pegar auth descoded

    var servletPath = request.getServletPath();

    if (servletPath.startsWith("/tasks")) {
      var authorazation = request.getHeader("Authorization");

      var authEncoded = authorazation.substring("Basic".length()).trim();

      byte[] authDecode = Base64.getDecoder().decode(authEncoded);

      var authString = new String(authDecode);

      String[] credentials = authString.split(":");
      var username = credentials[0];
      var password = credentials[1];

      System.out.println(username);
      System.out.println(password);
      // validar usuario

      var user = this.userRepository.findByUsername(username);
      System.out.println("estou no filter");

      if (user == null) {
        response.sendError(401);
      } else {
        // validar senha decode
        var passordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (passordVerify.verified) {
          System.out.println("passou na auth");
          request.setAttribute("idUser", user.getId());
          filterChain.doFilter(request, response);
        } else {
          response.sendError(401);
        }
      }

    } else {
      filterChain.doFilter(request, response);
    }

    // segue
    // throw new UnsupportedOperationException("Unimplemented method
    // 'doFilterInternal'");
  }

}