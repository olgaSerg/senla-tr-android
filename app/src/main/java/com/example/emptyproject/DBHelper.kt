package com.example.emptyproject

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

const val TABLE_NAME = "comment"
const val ID_COlUMN = "id"
const val POST_ID_COLUMN = "postId"
const val USER_ID_COLUMN = "userId"
const val TEXT_COLUMN = "text"


class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "PostsDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("msg", "--- onCreate database ---")

        db.execSQL(
            """
            CREATE TABLE $TABLE_NAME (
                $ID_COlUMN INTEGER PRIMARY KEY AUTOINCREMENT,
                $POST_ID_COLUMN INTEGER,
                $USER_ID_COLUMN INTEGER,
                $TEXT_COLUMN TEXT
            );
            """
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS "user" (
            	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
            	"firstName"	TEXT NOT NULL,
            	"lastName"	TEXT NOT NULL,
            	"email"	TEXT NOT NULL
            );
            """
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS "post" (
            	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
            	"userId"	INTEGER NOT NULL,
            	"title"	TEXT NOT NULL,
            	"body"	TEXT NOT NULL,
            	"rate"	INTEGER NOT NULL
            );
            """
        )

        db.execSQL(
            """
            INSERT INTO "comment" ("id","postId","userId","text") 
            VALUES (1,1,1,'This shot blew my mind.'),
                   (2,2,2,'It''s amazing not just slick!'),
                   (3,1,3,'Immensely thought out! Congrats on the new adventure!!'),
                   (4,4,4,'Revolutionary. I approve the use of iconography and gradient!'),
                   (5,3,2,'Lavender. I''d love to see a video of how it works.');
            """
        )
        db.execSQL(
            """
            INSERT INTO "user" ("id","firstName","lastName","email") 
            VALUES (1,'Kazuo','Bertram','kbertram@domain.tld'),
                   (2,'Goronwy','Gruffydd','ggruffydd@domain.tld'),
                   (3,'Celestino','Marius','cmarius@domain.tld'),
                   (4,'Selma','Davorka','sdavorka@domain.tld'),
                   (5,'Keelin','Renata','krenata@domain.tld');
            """
        )
        db.execSQL(
            """
            INSERT INTO "post" ("id","userId","title","body","rate") 
            VALUES (1,1,'When Growths Send You Running for Cover','Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, ',3),
                   (2,4,'Warning: You''re Losing Money by Not Using Customer Services','Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur? At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere ',4),
                   (3,5,'Why the Next 10 Years of Your Opinions Will Smash the Last 10','Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild Question Marks and devious Semikoli, but the Little Blind Text didn’t listen. She packed her seven versalia, put her initial into the belt and made herself on the way. When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her hometown Bookmarksgrove, the headline of Alphabet Village and the subline of her own road, the Line Lane. Pityful a rethoric question ran over her cheek, then ',5),
                   (4,1,'What Jezebel Should Write About The Customer Experience','One morning, when Gregor Samsa woke from troubled dreams, he found himself transformed in his bed into a horrible vermin. He lay on his armour-like back, and if he lifted his head a little he could see his brown belly, slightly domed and divided by arches into stiff sections. The bedding was hardly able to cover it and seemed ready to slide off any moment. His many legs, pitifully thin compared with the size of the rest of him, waved about helplessly as he looked. "What''s happened to me?" he thought. It wasn''t a dream. His room, a proper human room although a little too small, lay peacefully between its four familiar walls. A collection of textile samples lay spread out on the table - Samsa was a travelling salesman - and above it there hung a picture that he had recently cut out of an illustrated magazine and housed in a nice, gilded frame. It showed a lady fitted out with a fur hat and fur boa who sat upright, raising a heavy fur muff that covered the whole of her lower arm towards the viewer. Gregor then turned to look out the window at the dull weather. Drops ',1),
                   (5,2,'The Dummies'' Guide to A Challenge','A wonderful serenity has taken possession of my entire soul, like these sweet mornings of spring which I enjoy with my whole heart. I am alone, and feel the charm of existence in this spot, which was created for the bliss of souls like mine. I am so happy, my dear friend, so absorbed in the exquisite sense of mere tranquil existence, that I neglect my talents. I should be incapable of drawing a single stroke at the present moment; and yet I feel that I never was a greater artist than now. When, while the lovely valley teems with vapour around me, and the meridian sun strikes the upper surface of the impenetrable foliage of my trees, and but a few stray gleams steal into the inner sanctuary, I throw myself down among the tall grass by the trickling stream; and, as I lie close to the earth, a thousand unknown plants are noticed by me: when I hear the buzz of the little world among the stalks, and grow familiar with the countless indescribable forms of the insects and flies, then I feel the presence of the Almighty, who formed us in his own image, and the breath ',4);
        """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("msg", "--- onUpdate database ---")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}