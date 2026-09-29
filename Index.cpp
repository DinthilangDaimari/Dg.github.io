#include <iostream>
#include <Aspose.Words.Cpp/Document.h>
#include <Aspose.Words.Cpp/DocumentBuilder.h>

// Crucial namespaces for Aspose's object management engine
using System::SharedPtr;
using System::MakeObject;
using namespace Aspose::Words;

int main() {
    try {
        // 1. Correctly instantiate using Aspose's System Smart Pointers
        SharedPtr<Document> doc = MakeObject<Document>();
        SharedPtr<DocumentBuilder> builder = MakeObject<DocumentBuilder>(doc);

        // 2. Insert your image (PNG, JPG, etc.)
        builder->InsertImage(u"InputPhoto.jpg");

        // 3. Save the document directly as a PDF
        doc->Save(u"OutputDocument.pdf");
        
        std::cout << "PDF generated successfully!\n";
    }
    catch (const System::Exception& ex) {
        std::cerr << "Aspose Error: " << ex->get_Message() << std::endl;
        return 1;
    }
    
    return 0;
}
