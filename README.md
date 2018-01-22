# Geometry

Klasa wczytuje obrazek .bmp zawierający czarne figury na białym tle (metoda _load_image_) i wykrywa na nim prostokąty i elipsy. Wykrywanie kształtu jest wykonywane poprzez wykrywanie najbardziej wysuniętej czarnej pikseli do góry i do lewej strony (metoda _findShapes_). Są dwie metody wykorzystujące znalezione kształty do określenia jakiego typu jest to kształt: 
* _findRectangle_
* _findEllipses_

Program podaje również ułożenie kształtów względem siebie dotyczące styczności, zawieralności i przecinania się.

By dokonać przesunięcia jdnej z figur należy podać numer tej figury w konsoli i wektor przemieszczenia. Jeżeli przemieszczenie mieści się w obrazie zostanie ono wykonane. Następnie ułożenie względem siebie kształtów zostaje przekalkulowane na nowo.


# Hotel

Klasa zawierająca metodę _main_ to _Hotel.java_

Po uruchomieniu, można zalogować się jako użytkownik:
  username: _a_
  password: _a_

Aby dostać się do panelu admina należy wpisać w konsoli _admin_ by pojawiły się opcje dostępne tylko administratorowi.
