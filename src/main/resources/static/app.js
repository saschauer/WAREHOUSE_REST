$(document).ready(function() {

    let allProducts = [];

    function renderTable(products) {
        const tableBody = $('#product-table-body');
        tableBody.empty();

        if (products.length === 0) {
            tableBody.html('<tr><td colspan="5">Keine Produkte gefunden.</td></tr>');
            return;
        }

        products.forEach(function(product) {
            const rowHtml = `
                <tr>
                    <td>${product.productID}</td>
                    <td>${product.productName}</td>
                    <td>${product.productCategory}</td>
                    <td>${product.productQuantity}</td>
                    <td>${product.productUnit}</td>
                </tr>
            `;
            tableBody.append(rowHtml);
        });
    }

    $('#location-selector').on('change', function() {
        const locationId = $(this).val();

        if (!locationId) {
            renderTable([]);
            return;
        }

        const apiUrl = `http://localhost:8080/warehouse/${locationId}/data`;
        console.log("Frage API an unter:", apiUrl);

        $('#product-table-body').html('<tr><td colspan="5">Lade Daten...</td></tr>');

        $.ajax({
            url: apiUrl,
            method: "GET",
            dataType: "json",
            success: function(data) {
                console.log("Daten erfolgreich erhalten:", data);
                allProducts = data.productData || []; //
                renderTable(allProducts);
            },
            error: function(error) {
                console.error("Fehler beim Abrufen der Daten:", error);
                $('#product-table-body').html('<tr><td colspan="5">Fehler: Daten für diesen Standort konnten nicht geladen werden. Läuft die API?</td></tr>');
            }
        });
    });

    $('#product-name-filter').on('keyup', function() {
        const filterValue = $(this).val().toLowerCase();

        const filteredProducts = allProducts.filter(function(product) {
            return product.productName.toLowerCase().includes(filterValue);
        });

        renderTable(filteredProducts);
    });

});